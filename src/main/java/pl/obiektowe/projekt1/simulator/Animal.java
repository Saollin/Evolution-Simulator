package pl.obiektowe.projekt1.simulator;

public class Animal {
    private Position position;
    private int energy;
    private Genotype genotypeOfAnimal;

    public Animal(Position startPosition, int startEnergy){
        this.position = startPosition;
        this.energy = startEnergy;
        this.genotypeOfAnimal = new Genotype();
    }

    public Animal(Position startPosition, int startEnergy, int [] genotype){
        this.position = startPosition;
        this.energy = startEnergy;
        this.genotypeOfAnimal = new Genotype(genotype);
    }


}
