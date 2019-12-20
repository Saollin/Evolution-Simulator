package pl.obiektowe.projekt1.simulator.Classes;

import java.util.*;

public class StatisticOfDay {
    private int numberOfAnimal;
    private int numberOfPlants;
    private HashMap<Integer, LinkedList<Genotype>> dominantGenotypes;
    private int numberOfDominantGenotypes;
    private double averageEnergyOfLivingAnimals;
    private double averageLifetimeOfDeadAnimals;
    private double averageNumberChildOfLivingAnimals;

    public StatisticOfDay(int numberOfAnimal, int numberOfPlants, HashMap<Integer, LinkedList<Genotype>> dominantGenotypes,
            double averageEnergyOfLivingAnimals, double averageLifetimeOfDeadAnimals, double averageNumberChildOfLivingAnimals,
            int numberOfDominantGenotypes) {
        this.numberOfAnimal = numberOfAnimal;
        this.numberOfPlants = numberOfPlants;
        this.dominantGenotypes = dominantGenotypes;
        this.averageEnergyOfLivingAnimals = averageEnergyOfLivingAnimals;
        this.averageLifetimeOfDeadAnimals = averageLifetimeOfDeadAnimals;
        this.averageNumberChildOfLivingAnimals = averageNumberChildOfLivingAnimals;
        ArrayList<Integer> integers = new ArrayList<>(dominantGenotypes.keySet());
        Collections.sort(integers, Collections.reverseOrder());
        this.numberOfDominantGenotypes = numberOfDominantGenotypes;
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

    public int getNumberOfDominantGenotypes() {
        return numberOfDominantGenotypes;
    }

}
