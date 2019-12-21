package pl.obiektowe.projekt1.simulator.Classes;

import org.json.simple.JSONObject;
import pl.obiektowe.projekt1.simulator.Interfaces.IStatisticObserver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Log implements IStatisticObserver {
    public LinkedList<StatisticOfDay> statics;

    private LinkedList<Animal> deadAnimals = new LinkedList<>();

    @Override
    public void makeStatisticOfDay(LinkedList<Animal> animals, int numberOfPlants, LinkedList<Animal> deadAnimals) {
        int numberOfAnimal = animals.size();
        HashMap<Integer, LinkedList<Genotype>> dominantGenotype = countDominantGenotype(genotypesOfAnimals(animals));
        double averageEnergy = countAverageEnergyOfLivingAnimals(animals);
        double averageLifetime = countAverageLifetimeOfDeadAnimals(deadAnimals);
        double averageChildNumber = countAverageNumberChildOfLivingAnimals(animals);
        int numberOfDominantGenotypes = countNumberOfDominantGenotypes(dominantGenotype);
        StatisticOfDay oneDay = new StatisticOfDay(animals.size(), numberOfPlants, dominantGenotype,
                averageEnergy, averageLifetime, averageChildNumber, numberOfDominantGenotypes);
        statics.add(oneDay);
    }

    public void saveAverageStaticAfterGivenNumberOfDay(int numberOfDays) throws IOException{
        StatisticOfDay averageStatistic = countAverageStatisticAfterGivenNumberOfDay(numberOfDays);
        JSONObject jsonStatistic = averageStatistic.toJSONObject();
        try (FileWriter file = new FileWriter("statistic.json")) {

            file.write(jsonStatistic.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<Integer, LinkedList<Genotype>> countDominantGenotype(LinkedList<Genotype> genotypes) {
        HashMap<Genotype, Integer> genotypesWithIntegers = new HashMap<>();
        for (Genotype genotype : genotypes) {
            if (genotypesWithIntegers.containsKey(genotype)) {
                Integer previousValue = genotypesWithIntegers.get(genotype);
                genotypesWithIntegers.replace(genotype, previousValue.intValue() + 1);
            } else {
                genotypesWithIntegers.put(genotype, 1);
            }
        }
        ArrayList<Genotype> genotypesFromFirstHashMap = new ArrayList<>(genotypesWithIntegers.keySet());
        HashMap<Integer, LinkedList<Genotype>> integersWithListOfGenotypes = new HashMap<>();
        for (Genotype genotype : genotypesFromFirstHashMap) {
            LinkedList<Genotype> tmp;
            int howMany = genotypesWithIntegers.get(genotype).intValue();
            if (integersWithListOfGenotypes.containsKey(howMany)) {
                integersWithListOfGenotypes.get(howMany).add(genotype);
            } else {
                tmp = new LinkedList<>();
                tmp.add(genotype);
                integersWithListOfGenotypes.put(howMany, tmp);
            }
        }
        return integersWithListOfGenotypes;
    }

    private LinkedList<Genotype> genotypesOfAnimals(LinkedList<Animal> animals){
        LinkedList<Genotype> genotypes = new LinkedList<>();
        for(Animal animal : animals){
            genotypes.add(animal.getGenotypeOfAnimal());
        }
        return genotypes;
    }

    private int countNumberOfDominantGenotypes(HashMap<Integer, LinkedList<Genotype>> dominantGenotypes) {
        ArrayList<Integer> integers = new ArrayList<>(dominantGenotypes.keySet());
        Collections.sort(integers, Collections.reverseOrder());
        return integers.get(0).intValue();
    }

    private double countAverageEnergyOfLivingAnimals(LinkedList<Animal> animals) {
        double sumOfEnergy = 0;
        for (Animal animal : animals) {
            sumOfEnergy += animal.getEnergy();
        }
        return sumOfEnergy / animals.size();
    }

    private double countAverageLifetimeOfDeadAnimals(LinkedList<Animal> newDeadAnimals) {
        deadAnimals.addAll(newDeadAnimals);
        double sumOfLifetime = 0;
        for (Animal animal : deadAnimals) {
            sumOfLifetime += animal.getLifetime();
        }
        return sumOfLifetime / deadAnimals.size();
    }

    private double countAverageNumberChildOfLivingAnimals(LinkedList<Animal> animals) {
        double sumOfChild = 0;
        for (Animal animal : animals) {
            sumOfChild += animal.getNumberOfChild();
        }
        return sumOfChild / animals.size();
    }

    private StatisticOfDay countAverageStatisticAfterGivenNumberOfDay(int numberOfDays) {
        double numberOfAnimal = 0;
        double numberOfPlants = 0;
        double averageEnergy = 0;
        double averageLifetime = 0;
        double averageChildNumber = 0;
        LinkedList<Genotype> unorderedGenotypes = new LinkedList<>();
        ListIterator<StatisticOfDay> listIterator = statics.listIterator(0);
        while(listIterator.hasNext() && listIterator.nextIndex() != numberOfDays){
            StatisticOfDay day = listIterator.next();
            numberOfAnimal += day.getNumberOfAnimal();
            numberOfPlants += day.getNumberOfPlants();
            averageEnergy += day.getAverageEnergyOfLivingAnimals();
            averageLifetime += day.getAverageLifetimeOfDeadAnimals();
            averageChildNumber += day.getAverageNumberChildOfLivingAnimals();
            unorderedGenotypes.add(day.getDominantGenotypes().get(day.getNumberOfDominantGenotypes()).getFirst());
        }
        numberOfAnimal /= numberOfDays;
        numberOfPlants /= numberOfDays;
        averageEnergy /= numberOfDays;
        averageLifetime /= numberOfDays;
        averageChildNumber /= numberOfDays;
        HashMap<Integer, LinkedList<Genotype>> averageDominantGenotypes = countDominantGenotype(unorderedGenotypes);
        int numberOfDominantGenotypes = countNumberOfDominantGenotypes(averageDominantGenotypes);
        return new StatisticOfDay((int) numberOfAnimal, (int) numberOfPlants, averageDominantGenotypes,
                averageEnergy,averageLifetime,averageChildNumber,numberOfDominantGenotypes);
    }
}
