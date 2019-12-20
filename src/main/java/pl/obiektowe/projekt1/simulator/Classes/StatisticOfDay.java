package pl.obiektowe.projekt1.simulator.Classes;

import java.util.HashMap;
import java.util.LinkedList;

public class StatisticOfDay {
    private int numberOfAnimal;
    private int numberOfPlants;
    private HashMap<Integer, LinkedList<Genotype>> dominantGenotypes;
    private double averageEnergyOfLivingAnimals;
    private double averageLifetimeOfDeadAnimals;
    private double averageNumberChildOfLivingAnimals;

    public StatisticOfDay(int numberOfAnimal, int numberOfPlants, HashMap<Integer, LinkedList<Genotype>> dominantGenotypes,
            double averageEnergyOfLivingAnimals, double averageLifetimeOfDeadAnimals, double averageNumberChildOfLivingAnimals) {
        this.numberOfAnimal = numberOfAnimal;
        this.numberOfPlants = numberOfPlants;
        this.dominantGenotypes = dominantGenotypes;
        this.averageEnergyOfLivingAnimals = averageEnergyOfLivingAnimals;
        this.averageLifetimeOfDeadAnimals = averageLifetimeOfDeadAnimals;
        this.averageNumberChildOfLivingAnimals = averageNumberChildOfLivingAnimals;
    }

    public int getNumberOfAnimal() {
        return numberOfAnimal;
    }

    public int getNumberOfPlants() {
        return numberOfPlants;
    }

    public HashMap<Integer, LinkedList<Genotype>> getDominantGenotypes() {
        return dominantGenotypes;
    }

    public double getAverageEnergyOfLivingAnimals() {
        return averageEnergyOfLivingAnimals;
    }

    public double getAverageLifetimeOfDeadAnimals() {
        return averageLifetimeOfDeadAnimals;
    }

    public double getAverageNumberChildOfLivingAnimals() {
        return averageNumberChildOfLivingAnimals;
    }
}
