package pl.obiektowe.projekt1.simulator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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

    public EvolutionSimulatorMap(int width, int height, double jungleRatio, int startEnergy, int moveEnergy, int plantEnergy) {
        this.width = width;
        this.height = height;
        this.mapLowerLeft = new Vector2d(0,0);
        this.mapUpperRight = new Vector2d(width-1,height-1);

        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.startEnergy = (-1) * startEnergy;

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
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {

    }

    @Override
    public boolean place(IMapElement element) {
        if(element instanceof Animal){
            addAnimal((Animal) element);
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

    public boolean addAnimal(Animal newAnimal){
        if(newAnimal == null) return false;
        LinkedList<Animal> list = animals.get(newAnimal.getPosition());
        if(list == null){
            LinkedList<Animal> pom = new LinkedList<>();
            pom.add(newAnimal);
            animals.put(newAnimal.getPosition(), pom);
        }
        else{
            list.add(newAnimal);
        }
        animalList.add(newAnimal);
        return true;
    }

    public boolean removeAnimal(Animal animal){
        LinkedList<Animal> list = animals.get(animal.getPosition());
        if(list == null)
            throw new IllegalArgumentException("Animal on position:" + animal.getPosition() + "already not exist");
        else if(list.size() == 0){
            throw new IllegalArgumentException("Animal on position:" + animal.getPosition() + "already not exist");
        }
        else{
            list.remove(animal);
            if(list.size() == 0)
                animals.remove(animal.getPosition());
        }
        return true;
    }

    public void removeDeadAnimals(){
        for(Animal a:animalList){
            if(a.isDead()){
                removeAnimal(a);
                a.removeObservers(this);
            }
        }
    }

    public void moveAllAnimals(){
        for(Animal a:animalList){
            a.rotate();
            a.move(MoveDirection.FORWARD);
        }
    }



    @Override
    public Object objectAt(Vector2d position) {
        return null;
    }
}
