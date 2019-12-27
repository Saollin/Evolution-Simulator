package pl.obiektowe.projekt1.simulator.Classes;

import javafx.scene.paint.Color;
import pl.obiektowe.projekt1.simulator.DataModel.Log;
import pl.obiektowe.projekt1.simulator.DataModel.StartParameters;
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
    public Log statistics;
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
        this.jungleWidth = (int) (jungleRatio * width);
        this.jungleHeight = (int) (jungleRatio * height);

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

        statistics = new Log();
    }

    public EvolutionSimulatorMap(StartParameters parameters){
        this(parameters.getWidth(), parameters.getHeight(), parameters.getJungleRatio() / 100.0,
                parameters.getStartEnergy(), parameters.getMoveEnergy(), parameters.getPlantEnergy());
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
        removePositionOfAnimal((Animal) a, oldPosition);
        addPositionOfAnimal((Animal) a, newPosition);
    }

    @Override
    public boolean place(IMapElement element) {
        if (element instanceof Animal) {
            addPositionOfAnimal((Animal) element, element.getPosition());
            ((Animal) element).addObservers(this);
            animalList.add((Animal) element);
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

    public boolean addPositionOfAnimal(Animal newAnimal, Vector2d position) {
        if (newAnimal == null) return false;
        LinkedList<Animal> list = animals.get(position);
        if (list == null) {
            LinkedList<Animal> pom = new LinkedList<>();
            pom.add(newAnimal);
            animals.put(position, pom);
        } else {
            list.add(newAnimal);
        }
        return true;
    }

    //call this method as final in the day
    public boolean removePositionOfAnimal(Animal animal, Vector2d position) {
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
        LinkedList<Animal> allAnimals = animalList;
        for (Animal a : allAnimals) {
            if (a.isDead()) {
                removePositionOfAnimal(a, a.getPosition());
                a.removeObservers(this);
                deadAnimals.add(a);
            }
        }
        for(Animal dead: deadAnimals){
            animalList.remove(dead);
        }
        return deadAnimals;
    }

    public void moveAllAnimals() {
        LinkedList<Animal> allAnimal = animalList;
        for (Animal a : allAnimal) {
            a.rotate();
            a.move(MoveDirection.FORWARD);
            a.changeEnergy(moveEnergy);
            a.increaseLifetime();
        }
    }

    public void eating() {
        LinkedList<Plant> eatedPlants = new LinkedList<>();
        ArrayList<Vector2d> positionsOfAnimals = new ArrayList<>(animals.keySet());
        LinkedList<Animal> hungryAnimals;
        for(Vector2d position : positionsOfAnimals){
            hungryAnimals = animals.get(position);
            if(plants.get(position)!=null){
                if(hungryAnimals.size() == 1){
                    hungryAnimals.get(0).changeEnergy(plantEnergy);
                }
                else if (hungryAnimals.size() > 1){
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
                }
                eatedPlants.add(plants.get(position));
            }
        }
        for (Plant p : eatedPlants) {
            plants.remove(p.getPosition());
        }
    }

    public void reproduction() {
        LinkedList<Animal> childs = new LinkedList<>();
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
                    childs.add(child);
                    firstParent.increaseNumberOfChild();
                    secondParent.increaseNumberOfChild();
                }
            }
        }
        for(Animal child : childs) {
            place(child);
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
            directions.remove(Integer.valueOf(index));
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
        if(animals.size() == 0 || animals.size()/sizeOfJungle < 0.8){
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
        if(!isOneOrLessAnimal()) {
            makeStatistics(animalList, plants.size(), deadAnimals);
        }
    }


    public void makeStatistics(LinkedList<Animal> animals, int numberOfPlants, LinkedList<Animal> deadAnimals) {
        statistics.makeStatisticOfDay(animals, numberOfPlants, deadAnimals);
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

//    public java.awt.Color colorForAnimal(Animal animal) {
//        if (animal.getEnergy() == 0) return new java.awt.Color(222, 221, 224);
//        if (animal.getEnergy() < 0.2 * startEnergy) return new java.awt.Color(255, 157, 169, 255);
//        if (animal.getEnergy() < 0.4 * startEnergy) return new java.awt.Color(255, 110, 124);
//        if (animal.getEnergy() < 0.6 * startEnergy) return new java.awt.Color(255, 0, 30, 255);
//        if (animal.getEnergy() < 0.8 * startEnergy) return new java.awt.Color(226, 0, 32);
//        if (animal.getEnergy() < startEnergy) return new java.awt.Color(205, 0, 32);
//        if (animal.getEnergy() < 2 * startEnergy) return new java.awt.Color(179, 0, 31);
//        if (animal.getEnergy() < 4 * startEnergy) return new java.awt.Color(153, 0, 30);
//        if (animal.getEnergy() < 6 * startEnergy) return new java.awt.Color(131, 0, 28);
//        if (animal.getEnergy() < 8 * startEnergy) return new java.awt.Color(112, 0, 28);
//        if (animal.getEnergy() < 10 * startEnergy) return new java.awt.Color(87, 0, 22);
//        return new java.awt.Color(75, 0, 20);
//    }

    public Color colorForAnimal(Animal animal) {
        if (animal.getEnergy() == 0) return Color.web("rgb(222, 221, 224)");
        if (animal.getEnergy() < 0.2 * startEnergy) return Color.web("rgb(255, 157, 169)");
        if (animal.getEnergy() < 0.4 * startEnergy) return Color.web("rgb(255, 110, 124)");
        if (animal.getEnergy() < 0.6 * startEnergy) return Color.web("rgb(255, 0, 30)");
        if (animal.getEnergy() < 0.8 * startEnergy) return Color.web("rgb(226, 0, 32)");
        if (animal.getEnergy() < startEnergy) return Color.web("rgb(205, 0, 32)");
        if (animal.getEnergy() < 2 * startEnergy) return Color.web("rgb(179, 0, 31)");
        if (animal.getEnergy() < 4 * startEnergy) return Color.web("rgb(153, 0, 30)");
        if (animal.getEnergy() < 6 * startEnergy) return Color.web("rgb(131, 0, 28)");
        if (animal.getEnergy() < 8 * startEnergy) return Color.web("rgb(112, 0, 28)");
        if (animal.getEnergy() < 10 * startEnergy) return Color.web("rgb(87, 0, 22)");
        return Color.web("rgb(75, 0, 20)");
    }

    public boolean isOneOrLessAnimal(){
        return animalList.size()  <= 1;
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

    public int getJungleWidth() {
        return jungleWidth;
    }

    public int getJungleHeight() {
        return jungleHeight;
    }

    public Vector2d getJungleUpperRight() {
        return jungleUpperRight;
    }

    public Vector2d getJungleLowerLeft() {
        return jungleLowerLeft;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
