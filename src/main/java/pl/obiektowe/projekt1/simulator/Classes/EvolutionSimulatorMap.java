package pl.obiektowe.projekt1.simulator.Classes;

import pl.obiektowe.projekt1.simulator.Enum.MapDirection;
import pl.obiektowe.projekt1.simulator.Enum.MoveDirection;
import pl.obiektowe.projekt1.simulator.Interfaces.IMapElement;
import pl.obiektowe.projekt1.simulator.Interfaces.IPositionChangeObserver;
import pl.obiektowe.projekt1.simulator.Interfaces.IStatisticObserver;
import pl.obiektowe.projekt1.simulator.Interfaces.IWorldMap;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class EvolutionSimulatorMap implements IPositionChangeObserver, IWorldMap {

    //vectors of Map
    private final Vector2d mapUpperRight;
    private final Vector2d mapLowerLeft;
    private final Vector2d jungleUpperRight;
    private final Vector2d jungleLowerLeft;
    private final int width;
    private final int height;

    //vectors of Jungle
    private final int jungleWidth;
    private final int jungleHeight;
    private final double jungleRatio;

    //Energy
    private int startEnergy;
    private int moveEnergy;
    private int plantEnergy;

    //data of object
    private Map<Vector2d, Plant> plants = new HashMap<>();
    private Map<Vector2d, LinkedList<Animal>> animals = new HashMap<>();
    private LinkedList<Animal> animalList = new LinkedList<>();

    //statistic observers
    private ArrayList<IStatisticObserver> statisticObservers = new ArrayList<>();

    //static arrayList to generate random Positions in Jungle or Steppe
    private static ArrayList<Vector2d> jungle;
    private static ArrayList<Vector2d> steppe;

    public EvolutionSimulatorMap(int width, int height, double jungleRatio, int startEnergy, int moveEnergy, int plantEnergy) {
        this.width = width;
        this.height = height;
        this.mapLowerLeft = new Vector2d(0, 0);
        this.mapUpperRight = new Vector2d(width - 1, height - 1);

        this.moveEnergy = (-1) * moveEnergy;
        this.plantEnergy = plantEnergy;
        this.startEnergy = startEnergy;

        this.jungleRatio = jungleRatio;
        this.jungleWidth = (int) jungleRatio * width;
        this.jungleHeight = (int) jungleRatio * height;

        //coordinates of jungle
        int xLowerLeft = (int) (width * 0.5 - 0.5 * jungleWidth);
        int yLowerLeft = (int) (height * 0.5 - 0.5 * jungleHeight);
        int xUpperRight = (int) (width * 0.5 + 0.5 * jungleWidth);
        int yUpperRight = (int) (height * 0.5 + 0.5 * jungleHeight);

        this.jungleLowerLeft = new Vector2d(xLowerLeft, yLowerLeft);
        this.jungleUpperRight = new Vector2d(xUpperRight, yUpperRight);

        steppe = new ArrayList<>();
        jungle = new ArrayList<>();

        for (int i = mapLowerLeft.getX(); i < mapUpperRight.getX(); i++) {
            for (int j = mapLowerLeft.getY(); j < mapUpperRight.getY(); j++) {
                Vector2d position = new Vector2d(i, j);
                steppe.add(position);
            }
        }

        for (int i = jungleLowerLeft.getX(); i < jungleUpperRight.getX(); i++) {
            for (int j = jungleLowerLeft.getY(); j < jungleUpperRight.getY(); j++) {
                Vector2d position = new Vector2d(i, j);
                jungle.add(position);
                steppe.remove(position);
            }
        }
    }

    public Vector2d countRightPositionOnTheMap(Vector2d position) {
        int rightX;
        int rightY;

        if (position.getX() < mapLowerLeft.getX()) {
            rightX = width - Math.abs(position.getX());
        } else {
            rightX = position.getX() % width;
        }

        if (position.getY() < mapLowerLeft.getY()) {
            rightY = height - Math.abs(position.getY());
        } else {
            rightY = position.getY() % height;
        }
        return new Vector2d(rightX, rightY);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Object a) {
        removeAnimal((Animal) a, oldPosition);
        addAnimal((Animal) a, newPosition);
    }

    @Override
    public boolean place(IMapElement element) {
        if (element instanceof Animal) {
            addAnimal((Animal) element, element.getPosition());
            ((Animal) element).addObservers(this);
        }
        if (element instanceof Plant) {
            if (canPlantBePlaced(element.getPosition())) {
                plants.put(element.getPosition(), (Plant) element);
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean addAnimal(Animal newAnimal, Vector2d position) {
        if (newAnimal == null) return false;
        LinkedList<Animal> list = animals.get(position);
        if (list == null) {
            LinkedList<Animal> pom = new LinkedList<>();
            pom.add(newAnimal);
            animals.put(position, pom);
        } else {
            list.add(newAnimal);
        }
        animalList.add(newAnimal);
        return true;
    }

    //call this method as final in the day
    public boolean removeAnimal(Animal animal, Vector2d position) {
        LinkedList<Animal> list = animals.get(position);
        if (list == null)
            throw new IllegalArgumentException("Animal on position:" + animal.getPosition() + "already not exist");
        else if (list.size() == 0) {
            throw new IllegalArgumentException("Animal on position:" + animal.getPosition() + "already not exist");
        } else {
            list.remove(animal);
            if (list.size() == 0)
                animals.remove(position);
        }
        return true;
    }

    public LinkedList<Animal> removeDeadAnimals() {
        LinkedList<Animal> deadAnimals = new LinkedList<>();
        for (Animal a : animalList) {
            if (a.isDead()) {
                removeAnimal(a, a.getPosition());
                a.removeObservers(this);
                deadAnimals.add(a);
            }
        }
        return deadAnimals;
    }

    public void moveAllAnimals() {
        for (Animal a : animalList) {
            a.rotate();
            a.move(MoveDirection.FORWARD);
            a.changeEnergy(moveEnergy);
            a.increaseLifetime();
        }
    }

    public void eating() {
        LinkedList<Plant> eatedPlants = new LinkedList<>();

        for (Plant plant : plants.values()) {
            LinkedList<Animal> hungryAnimals = animals.get(plant.getPosition());
            if (hungryAnimals != null && hungryAnimals.size() > 0) {
                LinkedList<Animal> theStrongestOnes = new LinkedList<>();
                theStrongestOnes.add(hungryAnimals.get(0));
                for (int i = 1; i < hungryAnimals.size(); i++) {
                    if (theStrongestOnes.getFirst().getEnergy() < hungryAnimals.get(i).getEnergy()) {
                        theStrongestOnes.clear();
                        theStrongestOnes.add(hungryAnimals.get(i));
                    } else if (theStrongestOnes.getFirst().getEnergy() == hungryAnimals.get(i).getEnergy()) {
                        theStrongestOnes.add(hungryAnimals.get(i));
                    }
                }
                for (Animal animal : theStrongestOnes) {
                    animal.changeEnergy(plantEnergy / theStrongestOnes.size());
                }
                eatedPlants.add(plant);
            }
        }

        for (Plant p : eatedPlants) {
            plants.remove(p);
        }
    }

    public void reproduction() {
        for (LinkedList<Animal> animalList : animals.values()) {
            if (animalList != null && animalList.size() >= 2) {
                if (animalList.size() > 2) {
                    animalList.sort((o1, o2) -> o1.getEnergy() - o2.getEnergy());
                }
                Animal firstParent = animalList.get(animalList.size() - 1);
                Animal secondParent = animalList.get(animalList.size() - 2);
                if (canCopulate(firstParent) && canCopulate(secondParent)) {
                    Vector2d positionOfChild = randomPositionNextToOtherPosition(firstParent.getPosition());
                    Animal child = firstParent.copulate(secondParent, positionOfChild);
                    place(child);
                    firstParent.increaseNumberOfChild();
                    secondParent.increaseNumberOfChild();
                }
            }
        }
    }

    private boolean canCopulate(Animal potentialParent) {
        return potentialParent.getEnergy() >= 0.5 * startEnergy;
    }

    /**
     * This method returns empty Position next to other position.
     * If all position around are occupied it returns random position next to.
     *
     * @param position
     * @return
     */
    public Vector2d randomPositionNextToOtherPosition(Vector2d position) {
        ArrayList<Integer> directions = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            directions.add(i);
        }
        Random generator = new Random();
        Vector2d result;
        MapDirection direction;
        int index;
        do {
            index = directions.get(generator.nextInt(directions.size()));
            direction = MapDirection.valueOfDirectionNumber(index);
            result = countRightPositionOnTheMap(position.add(direction.toUnitVector()));
            directions.remove(index);
        } while (objectAt(result) != null && directions.size() != 0);
        return result;
    }

    public void spawnGrassInSteppeAndJungle() {
        spawnGrassIn(jungle);
        spawnGrassIn(steppe);
    }

    public void spawnGrassIn(ArrayList<Vector2d> area) {
        CopyOnWriteArrayList<Vector2d> positions = new CopyOnWriteArrayList<>(area);
        Random generator = new Random();
        while (positions.size() > 0) {
            Vector2d newPosition = positions.get(generator.nextInt(positions.size()));
            if (canPlantBePlaced(newPosition)) {
                place(new Plant(newPosition));
                break;
            } else {
                positions.remove(newPosition);
            }
        }
    }

    public boolean canPlantBePlaced(Vector2d position) {
        return objectAt(position) == null;
    }

    /**
     * This method is called only in the beginning of simulation and in most of cases only some times.
     * The Jungle will be empty in large part for small number of Animals, so then loop while should be break fast.
     * In case, when there would be over 80% of jungle occupied by animals, then we use other implementation choosing
     * position to place, which guarantees success.
     * @return if Animal was placed
     */
    public boolean placeAnimalInRandomFieldInJungle(){
        Random generator = new Random();
        int sizeOfJungle = jungleHeight * jungleWidth;
        if(animals.size()/sizeOfJungle < 0.8){
            int howMuchTime = 0;
            while(howMuchTime < sizeOfJungle){
                int newX = generator.nextInt(jungleWidth) + jungleLowerLeft.getX();
                int newY = generator.nextInt(jungleHeight) + jungleLowerLeft.getY();
                Vector2d position = new Vector2d(newX, newY);
                if(animals.get(position) == null){
                    place(new Animal(this, position, startEnergy));
                    return true;
                }
                howMuchTime++;
            }
        }
        else{
            CopyOnWriteArrayList<Vector2d> positionsInJungle = new CopyOnWriteArrayList<>(jungle);
            while (positionsInJungle.size() > 0) {
                int newX = generator.nextInt(jungleWidth) + jungleLowerLeft.getX();
                int newY = generator.nextInt(jungleHeight) + jungleLowerLeft.getY();
                Vector2d position = new Vector2d(newX, newY);
                if (animals.get(position) == null) {
                    place(new Animal(this, position, startEnergy));
                    return true;
                } else {
                    positionsInJungle.remove(position);
                }
            }
        }
        return false;
    }

    public void oneDay(){
        moveAllAnimals();
        eating();
        reproduction();
        spawnGrassInSteppeAndJungle();
        LinkedList<Animal> deadAnimals = removeDeadAnimals();
        notifyObservers(animalList, plants.size(), deadAnimals);
    }

    public void addObservers(IStatisticObserver observer){
        statisticObservers.add(observer);
    }

    public void removeObservers(IStatisticObserver observer){
        statisticObservers.remove(observer);
    }

    public void notifyObservers(LinkedList<Animal> animals, int numberOfPlants, LinkedList<Animal> deadAnimals){
        for(IStatisticObserver observer:statisticObservers){
            observer.makeStatisticOfDay(animalList, numberOfPlants, deadAnimals);
        }
    }
    @Override
    public Object objectAt(Vector2d position) {
        Object result;
        result = plants.get(position);
        if (result == null) {
            result = animals.get(position) == null ? null : animals.get(position).getFirst();
        }
        return result;
    }

    public Map<Vector2d, Plant> getPlants() {
        return plants;
    }

    public Map<Vector2d, LinkedList<Animal>> getAnimals() {
        return animals;
    }

    public LinkedList<Animal> getAnimalList() {
        return animalList;
    }
}