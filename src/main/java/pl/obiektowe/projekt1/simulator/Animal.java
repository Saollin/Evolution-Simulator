package pl.obiektowe.projekt1.simulator;

public class Animal implements IMapElement{
    private Vector2d position;
    private int energy;
    private Genotype genotypeOfAnimal;

    public Animal(Vector2d startPosition, int startEnergy){
        this.position = startPosition;
        this.energy = startEnergy;
        this.genotypeOfAnimal = new Genotype();
    }

    public Animal(Vector2d startPosition, int startEnergy, int [] genotype){
        this.position = startPosition;
        this.energy = startEnergy;
        this.genotypeOfAnimal = new Genotype(genotype);
    }


    @Override
    public Vector2d getPosition() {
        return this.position;
    }
}
