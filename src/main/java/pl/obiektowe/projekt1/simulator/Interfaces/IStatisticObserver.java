package pl.obiektowe.projekt1.simulator.Interfaces;

import pl.obiektowe.projekt1.simulator.Classes.Animal;

import java.util.LinkedList;

public interface IStatisticObserver {
    void makeStatisticOfDay(LinkedList<Animal> animals, int numberOfPlants, LinkedList<Animal> deadAnimals);
}
