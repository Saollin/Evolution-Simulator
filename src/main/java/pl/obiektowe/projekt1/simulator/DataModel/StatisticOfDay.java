package pl.obiektowe.projekt1.simulator.DataModel;

import org.json.simple.JSONObject;
import pl.obiektowe.projekt1.simulator.Classes.Genotype;

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

    public JSONObject toJSONObject(){
        JSONObject statisticOfDay = new JSONObject();
        statisticOfDay.put("Number of animals", numberOfAnimal);
        statisticOfDay.put("Number of plants", numberOfPlants);
        statisticOfDay.put("Dominant genotype", dominantGenotypes.get(numberOfDominantGenotypes).getFirst());
        statisticOfDay.put("Average energy of living animals",averageEnergyOfLivingAnimals);
        statisticOfDay.put("Average lifetime of dead animals", averageLifetimeOfDeadAnimals);
        statisticOfDay.put("Average number child of living animals", averageNumberChildOfLivingAnimals);
        return statisticOfDay;
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
