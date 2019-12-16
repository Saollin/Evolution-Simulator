package pl.obiektowe.projekt1.simulator;

import java.util.ArrayList;

public class Animal implements IMapElement{

    private MapDirection directionOfAnimal;
    private IWorldMap map;
    private Vector2d position;
    private int energy;
    private ArrayList<IPositionChangeObserver> observers = new ArrayList<>();
    private Genotype genotypeOfAnimal;

    public Animal(IWorldMap map, Vector2d startPosition, int startEnergy){
        this.map = map;
        this.position = startPosition;
        this.energy = startEnergy;
        this.directionOfAnimal = MapDirection.NORTH;
        this.genotypeOfAnimal = new Genotype();
    }

    public Animal(IWorldMap map, Vector2d startPosition, int startEnergy, Genotype genotype){
        this(map,startPosition,startEnergy);
        this.genotypeOfAnimal = genotype;
    }

    public boolean isDead(){
        return this.energy <= 0;
    }

    public void changeEnergy(int value){
        this.energy += value;
    }

    public void addObservers(IPositionChangeObserver observer){
        observers.add(observer);
    }

    public void removeObservers(IPositionChangeObserver observer){
        observers.remove(observer);
    }

    public void notifyObserver(Vector2d oldPosition, Vector2d newPosition){
        for(IPositionChangeObserver observer:observers){
            observer.positionChanged(oldPosition,newPosition);
        }
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }
}
