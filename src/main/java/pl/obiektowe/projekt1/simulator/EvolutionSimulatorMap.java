package pl.obiektowe.projekt1.simulator;

import java.util.*;

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
    private Map<Vector2d,Plant> plants = new HashMap<>();
    private Map<Vector2d, LinkedList<Animal>> animals = new HashMap<>();
    private LinkedList<Animal> animalList = new LinkedList<>();

    //static arrayList to generate random Positions in Jungle or Steppe
    private static ArrayList<Vector2d> allPositionsInJungle;
    private static ArrayList<Vector2d> allPositionsInSteppe;

    public EvolutionSimulatorMap(int width, int height, double jungleRatio, int startEnergy, int moveEnergy, int plantEnergy) {
        this.width = width;
        this.height = height;
        this.mapLowerLeft = new Vector2d(0,0);
        this.mapUpperRight = new Vector2d(width-1,height-1);

        this.moveEnergy = (-1) * moveEnergy;
        this.plantEnergy = plantEnergy;
        this.startEnergy = startEnergy;

        this.jungleRatio = jungleRatio;
        this.jungleWidth = (int) jungleRatio * width;
        this.jungleHeight = (int) jungleRatio * height;

        //coordinates of jungle
        int xLowerLeft = (int)(width * 0.5 - 0.5 * jungleWidth);
        int yLowerLeft = (int)(height * 0.5 - 0.5 * jungleHeight);
        int xUpperRight = (int)(width * 0.5 + 0.5 * jungleWidth);
        int yUpperRight = (int)(height * 0.5 + 0.5 * jungleHeight);

        this.jungleLowerLeft = new Vector2d(xLowerLeft, yLowerLeft);
        this.jungleUpperRight = new Vector2d(xUpperRight, yUpperRight);

        allPositionsInSteppe = new ArrayList<>();
        allPositionsInJungle = new ArrayList<>();

        for(int i = mapLowerLeft.getX(); i < mapUpperRight.getX(); i++){
            for(int j = mapLowerLeft.getY(); j < mapUpperRight.getY(); j++){
                Vector2d position = new Vector2d(i,j);
                allPositionsInSteppe.add(position);
            }
        }

        for(int i = jungleLowerLeft.getX(); i < jungleUpperRight.getX(); i++){
            for(int j = jungleLowerLeft.getY(); j < jungleUpperRight.getY(); j++){
                Vector2d position = new Vector2d(i,j);
                allPositionsInJungle.add(position);
                allPositionsInSteppe.remove(position);
            }
        }
    }

    public Vector2d countRightPositionOnTheMap(Vector2d position){
        int rightX;
        int rightY;

        if(position.getX() < mapLowerLeft.getX()){
            rightX = width - Math.abs(position.getX());
        }
        else{
            rightX = position.getX() % width;
        }

        if(position.getY() < mapLowerLeft.getY()){
            rightY = height - Math.abs(position.getY());
        }
        else{
            rightY = position.getY() %  height;
        }
        return new Vector2d(rightX,rightY);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Object a) {
        removeAnimal((Animal) a, oldPosition);
        addAnimal((Animal) a, newPosition);
    }

    @Override
    public boolean place(IMapElement element) {
        if(element instanceof Animal){
            addAnimal((Animal) element, element.getPosition());
            ((Animal) element).addObservers(this);
        }
        if(element instanceof Plant){
            if(plants.get(element.getPosition()) == null){
                plants.put(element.getPosition(), (Plant) element);
            }
            else{
                return false;
            }
        }
        return true;
    }

    public boolean addAnimal(Animal newAnimal, Vector2d position){
        if(newAnimal == null) return false;
        LinkedList<Animal> list = animals.get(position);
        if(list == null){
            LinkedList<Animal> pom = new LinkedList<>();
            pom.add(newAnimal);
            animals.put(position, pom);
        }
        else{
            list.add(newAnimal);
        }
        animalList.add(newAnimal);
        return true;
    }

    public boolean removeAnimal(Animal animal, Vector2d position){
        LinkedList<Animal> list = animals.get(position);
        if(list == null)
            throw new IllegalArgumentException("Animal on position:" + animal.getPosition() + "already not exist");
        else if(list.size() == 0){
            throw new IllegalArgumentException("Animal on position:" + animal.getPosition() + "already not exist");
        }
        else{
            list.remove(animal);
            if(list.size() == 0)
                animals.remove(position);
        }
        return true;
    }

    public void removeDeadAnimals(){
        for(Animal a:animalList){
            if(a.isDead()){
                removeAnimal(a, a.getPosition());
                a.removeObservers(this);
            }
        }
    }

    public void moveAllAnimals(){
        for(Animal a:animalList){
            a.rotate();
            a.move(MoveDirection.FORWARD);
            a.changeEnergy(moveEnergy);
        }
    }

    public void eating(){
        LinkedList<Plant> eatedPlants = new LinkedList<>();

        for(Plant plant:plants.values()){
            LinkedList<Animal> hungryAnimals = animals.get(plant.getPosition());
            if(hungryAnimals != null && hungryAnimals.size() > 0){
                LinkedList<Animal> theStrongestOnes = new LinkedList<>();
                theStrongestOnes.add(hungryAnimals.get(0));
                for(int i = 1; i < hungryAnimals.size(); i++){
                    if(theStrongestOnes.getFirst().getEnergy() < hungryAnimals.get(i).getEnergy()) {
                        theStrongestOnes.clear();
                        theStrongestOnes.add(hungryAnimals.get(i));
                    }
                    else if(theStrongestOnes.getFirst().getEnergy() == hungryAnimals.get(i).getEnergy()) {
                        theStrongestOnes.add(hungryAnimals.get(i));
                    }
                }
                for(Animal animal:theStrongestOnes){
                    animal.changeEnergy(plantEnergy/theStrongestOnes.size());
                }
                eatedPlants.add(plant);
            }
        }

        for(Plant p : eatedPlants){
            plants.remove(p);
        }
    }

    public void reproduction(){
        for(LinkedList<Animal> animalList : animals.values()){
            if(animalList != null && animalList.size() >= 2){
                if(animalList.size() > 2){
                    animalList.sort((o1, o2) -> o1.getEnergy() - o2.getEnergy());
                }
                Animal firstParent = animalList.get(animalList.size()-1);
                Animal secondParent = animalList.get(animalList.size()-2);
                if (canCopulate(firstParent) && canCopulate(secondParent)) {
                    Vector2d positionOfChild = randomPositionNextToOtherPosition(firstParent.getPosition());
                    Animal child = firstParent.copulate(secondParent,positionOfChild);
                    place(child);
                }
            }
        }
    }

    private boolean canCopulate(Animal potentialParent){
        return potentialParent.getEnergy() >= 0.5 * startEnergy;
    }

    /**
     * This method returns empty Position next to other position.
     * If all position around are occupied it returns random position next to.
     *
     * @param position
     * @return
     */
    public Vector2d randomPositionNextToOtherPosition(Vector2d position){
        ArrayList<Integer> directions = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            directions.add(i);
        }
        Random generator = new Random();
        Vector2d result;
        MapDirection direction;
        int index;
        do{
            index = directions.get(generator.nextInt(directions.size()));
            direction = MapDirection.valueOfDirectionNumber(index);
            result = countRightPositionOnTheMap(position.add(direction.toUnitVector()));
            directions.remove(index);
        }while(objectAt(result) != null && directions.size() != 0);
        return result;
    }

    public boolean canPlantBePlaced(Vector2d position){
        return objectAt(position) == null;
    }

    @Override
    public Object objectAt(Vector2d position) {
        Object result;
        result = plants.get(position);
        if(result == null){
            result = animals.get(position).getFirst();
        }
        return result;
    }

}
