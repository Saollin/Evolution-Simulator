package pl.obiektowe.projekt1.simulator.Classes;

import pl.obiektowe.projekt1.simulator.Interfaces.IStatisticObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Log implements IStatisticObserver {
    public LinkedList<StatisticOfDay> statics;

    @Override
    public void makeStatisticOfDay(LinkedList<Animal> animals, int numberOfPlants, LinkedList<Animal> deadAnimals) {
        int numberOfAnimal = animals.size();
        HashMap<Integer, LinkedList<Genotype>> dominantGenotype = countDominantGenotype(animals);
        double averageEnergy = countAverageEnergyOfLivingAnimals(animals);
        double averageLifetime = countAverageLifetimeOfDeadAnimals(deadAnimals);
        double averageChildNumber = countAverageNumberChildOfLivingAnimals(animals);
        StatisticOfDay oneDay = new StatisticOfDay(animals.size(), numberOfPlants, dominantGenotype,
                averageEnergy, averageLifetime, averageChildNumber);
        statics.add(oneDay);
    }

    private HashMap<Integer, LinkedList<Genotype>> countDominantGenotype(LinkedList<Animal> animals) {
        HashMap<Genotype, Integer> genotypesWithIntegers = new HashMap<>();
        for (Animal animal : animals) {
            Genotype genotype = animal.getGenotypeOfAnimal();
            if (genotypesWithIntegers.containsKey(genotype)) {
                Integer previousValue = genotypesWithIntegers.get(genotype);
                genotypesWithIntegers.replace(genotype, previousValue.intValue() + 1);
            } else {
                genotypesWithIntegers.put(genotype, 1);
            }
        }
        ArrayList<Genotype> genotypes = new ArrayList<>(genotypesWithIntegers.keySet());
        HashMap<Integer, LinkedList<Genotype>> integersWithListOfGenotypes = new HashMap<>();
        for (Genotype genotype : genotypes) {
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

    private double countAverageEnergyOfLivingAnimals(LinkedList<Animal> animals) {
        double sumOfEnergy = 0;
        for (Animal animal : animals) {
            sumOfEnergy += animal.getEnergy();
        }
        return sumOfEnergy / animals.size();
    }

    private double countAverageLifetimeOfDeadAnimals(LinkedList<Animal> deadAnimals) {
        double sumOfLifetime = 0;
        for (Animal animal : deadAnimals) {
            sumOfLifetime += animal.getLifetime();
        }
        return sumOfLifetime / deadAnimals.size();
    }

    ;

    private double countAverageNumberChildOfLivingAnimals(LinkedList<Animal> animals) {
        double sumOfChild = 0;
        for (Animal animal : animals) {
            sumOfChild += animal.getNumberOfChild();
        }
        return sumOfChild / animals.size();
    }
}
