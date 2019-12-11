package pl.obiektowe.projekt1.simulator;

public class Animal {
    private Position position;
    private int energy;

    public Animal(Position startPosition, int startEnergy){
        this.position = startPosition;
        this.energy = startEnergy;
    }
}
