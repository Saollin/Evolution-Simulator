package pl.obiektowe.projekt1.simulator.Visualization;

import pl.obiektowe.projekt1.simulator.Classes.EvolutionSimulatorMap;
import pl.obiektowe.projekt1.simulator.Classes.Genotype;
import pl.obiektowe.projekt1.simulator.Classes.Log;
import pl.obiektowe.projekt1.simulator.Classes.StatisticOfDay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class StatisticPanel extends JPanel implements ActionListener {

    private JLabel numberOfAnimalLabel;
    private JLabel numberOfPlantsLabel;
    private JLabel dominantGenotypesLabel;
    private JLabel averageEnergyOfLivingAnimalsLabel;
    private JLabel averageLifetimeOfDeadAnimalsLabel;
    private JLabel averageNumberChildOfLivingAnimalsLabel;

    private JLabel numberOfAnimal;
    private JLabel numberOfPlants;
    private JLabel dominantGenotypes;
    private JLabel averageEnergyOfLivingAnimals;
    private JLabel averageLifetimeOfDeadAnimals;
    private JLabel averageNumberChildOfLivingAnimals;

    private JButton saveButton;

    private EvolutionSimulatorMap map;

    public StatisticPanel(EvolutionSimulatorMap map) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        this.map = map;
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);

        //LABELS
        numberOfAnimalLabel = new JLabel("Number of animals: ");
        numberOfPlantsLabel = new JLabel("Number of plants: ");
        dominantGenotypesLabel = new JLabel("Dominant Genotype: ");
        averageEnergyOfLivingAnimalsLabel = new JLabel("Average energy of living animals: ");
        averageLifetimeOfDeadAnimalsLabel = new JLabel("Average lifetime of dead animals: ");
        averageNumberChildOfLivingAnimalsLabel = new JLabel("Average number of child: ");

        numberOfAnimal = new JLabel();
        numberOfPlants = new JLabel();
        dominantGenotypes = new JLabel();
        averageEnergyOfLivingAnimals = new JLabel();
        averageLifetimeOfDeadAnimals = new JLabel();
        averageNumberChildOfLivingAnimals = new JLabel();


        //Labels to text fields
        numberOfAnimalLabel.setLabelFor(numberOfAnimal);
        numberOfPlantsLabel.setLabelFor(numberOfPlants);
        dominantGenotypesLabel.setLabelFor(dominantGenotypes);
        averageEnergyOfLivingAnimalsLabel.setLabelFor(averageEnergyOfLivingAnimals);
        averageLifetimeOfDeadAnimalsLabel.setLabelFor(averageLifetimeOfDeadAnimals);
        averageNumberChildOfLivingAnimalsLabel.setLabelFor(averageNumberChildOfLivingAnimals);

        JPanel l1 = new JPanel();
        JPanel l2 = new JPanel();
        JPanel l3 = new JPanel();
        JPanel l4 = new JPanel();
        JPanel l5 = new JPanel();
        JPanel l6 = new JPanel();

        l1.add(numberOfAnimalLabel);
        l2.add(numberOfPlantsLabel);
        l3.add(dominantGenotypesLabel);
        l4.add(averageEnergyOfLivingAnimalsLabel);
        l5.add(averageLifetimeOfDeadAnimalsLabel);
        l6.add(averageNumberChildOfLivingAnimalsLabel);

        l1.add(numberOfAnimal);
        l2.add(numberOfPlants);
        l3.add(dominantGenotypes);
        l4.add(averageEnergyOfLivingAnimals);
        l5.add(averageLifetimeOfDeadAnimals);
        l6.add(averageNumberChildOfLivingAnimals);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);

        add(new JLabel("Statistics"));
        add(l1);
        add(l2);
        add(l3);
        add(l4);
        add(l5);
        add(l6);
        add(buttonPanel);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Log log = (Log) map.statisticObservers.get(0);
            log.saveAverageStaticAfterGivenNumberOfDay(log.statics.size());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void updateStatstics(StatisticOfDay statistic){
        numberOfAnimal.setText(statistic.getNumberOfAnimal() + "");
        numberOfPlants.setText(statistic.getNumberOfPlants() + "");
        Integer numberOfDominantGenotype = statistic.getNumberOfDominantGenotypes();
        dominantGenotypes.setText(statistic.getDominantGenotypes().get(numberOfDominantGenotype).getFirst().toString());
        averageEnergyOfLivingAnimals.setText(statistic.getAverageEnergyOfLivingAnimals() + "");
        averageLifetimeOfDeadAnimals.setText(statistic.getAverageLifetimeOfDeadAnimals() + "");
        averageNumberChildOfLivingAnimals.setText(statistic.getAverageEnergyOfLivingAnimals() + "");
    }
}
