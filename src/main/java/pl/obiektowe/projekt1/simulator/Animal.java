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

    public void notifyObservers(Vector2d oldPosition, Vector2d newPosition){
        for(IPositionChangeObserver observer:observers){
            observer.positionChanged(oldPosition,newPosition);
        }
    }

    //moving
    public void rotate(){
        MapDirection newDirection = MapDirection.valueOfDirectionNumber(this.genotypeOfAnimal.randomGen());
        this.directionOfAnimal = newDirection;
    }

    public void move(MoveDirection moveDirection){
        Vector2d unitVector, reverseUnitVector, newPosition, oldPosition;
        switch (moveDirection){
            case FORWARD:
                unitVector = this.directionOfAnimal.toUnitVector();
                oldPosition = this.getPosition();
                newPosition = map.countRightPositionOnTheMap(position.add(unitVector));
                this.position = newPosition;
                this.notifyObservers(oldPosition, newPosition);
                break;
            case BACKWARD:
                unitVector = this.directionOfAnimal.toUnitVector();
                oldPosition = this.getPosition();
                newPosition = map.countRightPositionOnTheMap(position.subtract(unitVector));
                this.position = newPosition;
                this.notifyObservers(oldPosition, newPosition);
                break;
            default:
                return;
        }
    }

    public Animal copulate(Animal secondParent, Vector2d positionOfChild){
        int childEnergy = (int)(0.25 * this.energy) + (int)(0.25 * secondParent.energy);
        this.changeEnergy((int)-(0.25 * this.energy));
        secondParent.changeEnergy((int)-(0.25 * secondParent.energy));
        Genotype genotypeOfChild = this.genotypeOfAnimal.createNewGenotypeWithSecondParent(secondParent.getGenotypeOfAnimal());
        return new Animal(map,positionOfChild,childEnergy, genotypeOfChild);
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public Genotype getGenotypeOfAnimal() {
        return genotypeOfAnimal;
    }

    public int getEnergy() {
        return energy;
    }

}
