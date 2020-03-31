package pl.obiektowe.projekt1.simulator.Swing;

import pl.obiektowe.projekt1.simulator.Classes.EvolutionSimulatorMap;
import pl.obiektowe.projekt1.simulator.Swing.MapSimulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPanel extends JPanel implements ActionListener {
        //
        public static final int HEIGHT = 600;
        public static final int WIDTH = 600;
        //Fields for data entry
        private JTextField delay;
        private JTextField animalsStartEnergy;
        private JTextField numOfSpawnedAnimals;
        private JTextField grassEatingEnergyProfit;
        private JTextField mapWidth;
        private JTextField mapHeight;
        private JTextField jungleRatio;
        private JTextField dayEnergyCost;
        private JTextField grassSpawnedInEachDay;
        //Labels to identify the fields
        private JLabel delayLabel;
        private JLabel animalsStartEnergyLabel;
        private JLabel numOfSpawnedAnimalsLabel;
        private JLabel grassEatingEnergyProfitLabel;
        private JLabel mapWidthLabel;
        private JLabel mapHeightLabel;
        private JLabel jungleRatioLabel;
        private JLabel dayEnergyCostLabel;
        private JLabel grassSpawnedInEachDayLabel;
        //button
        private JButton startButton;

        public SettingsPanel(Integer[] defaultMapProperties) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setPreferredSize(new Dimension(WIDTH, HEIGHT));

            startButton = new JButton("Start Simulation");
            startButton.addActionListener(this);

            //LABELS
            delayLabel = new JLabel("Real refresh time (ms):           ");
            animalsStartEnergyLabel = new JLabel("Animal start energy:              ");
            numOfSpawnedAnimalsLabel = new JLabel("Animals spawning at the start:    ");
            grassEatingEnergyProfitLabel = new JLabel("Grass energy profit:              ");
            mapHeightLabel = new JLabel("Map height:                       ");
            mapWidthLabel = new JLabel("Map width:                        ");
            jungleRatioLabel = new JLabel("Jungle ratio:                     ");
            dayEnergyCostLabel = new JLabel("Daily energy cost:                ");
            //TEXT FIELDS
            int a = 10;
            mapWidth = new JTextField();
            mapWidth.setColumns(a);
            mapWidth.setText(defaultMapProperties[0].toString());

            mapHeight = new JTextField();
            mapHeight.setColumns(a);
            mapHeight.setText(defaultMapProperties[1].toString());

            jungleRatio = new JTextField();
            jungleRatio.setColumns(a);
            jungleRatio.setText(defaultMapProperties[2].toString());

            animalsStartEnergy = new JTextField();
            animalsStartEnergy.setColumns(a);
            animalsStartEnergy.setText(defaultMapProperties[3].toString());

            dayEnergyCost = new JTextField();
            dayEnergyCost.setColumns(a);
            dayEnergyCost.setText(defaultMapProperties[4].toString());

            grassEatingEnergyProfit = new JTextField();
            grassEatingEnergyProfit.setColumns(a);
            grassEatingEnergyProfit.setText(defaultMapProperties[5].toString());

            delay = new JTextField();
            delay.setColumns(a);
            delay.setText(defaultMapProperties[6].toString());

            numOfSpawnedAnimals = new JTextField();
            numOfSpawnedAnimals.setColumns(a);
            numOfSpawnedAnimals.setText(defaultMapProperties[7].toString());

            //Labels to text fields
            delayLabel.setLabelFor(delay);
            animalsStartEnergyLabel.setLabelFor(animalsStartEnergy);
            numOfSpawnedAnimalsLabel.setLabelFor(numOfSpawnedAnimals);
            grassEatingEnergyProfitLabel.setLabelFor(grassEatingEnergyProfit);
            mapHeightLabel.setLabelFor(mapHeight);
            mapWidthLabel.setLabelFor(mapWidth);
            jungleRatioLabel.setLabelFor(jungleRatio);
            dayEnergyCostLabel.setLabelFor(dayEnergyCost);

            JPanel l1 = new JPanel();
            JPanel l2 = new JPanel();
            JPanel l3 = new JPanel();
            JPanel l4 = new JPanel();
            JPanel l5 = new JPanel();
            JPanel l6 = new JPanel();
            JPanel l7 = new JPanel();
            JPanel l8 = new JPanel();
            JPanel l10 = new JPanel();
            JPanel l11 = new JPanel();


            l1.add(delayLabel);
            l2.add(animalsStartEnergyLabel);
            l4.add(numOfSpawnedAnimalsLabel);
            l5.add(grassEatingEnergyProfitLabel);
            l6.add(mapHeightLabel);
            l7.add(mapWidthLabel);
            l8.add(jungleRatioLabel);
            l10.add(dayEnergyCostLabel);

            l1.add(delay);
            l2.add(animalsStartEnergy);
            l4.add(numOfSpawnedAnimals);
            l5.add(grassEatingEnergyProfit);
            l6.add(mapHeight);
            l7.add(mapWidth);
            l8.add(jungleRatio);
            l10.add(dayEnergyCost);

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(startButton);

            add(new JLabel("Map properties"));
            add(l6);
            add(l7);
            add(l8);
            add(new JLabel("Energy properties"));
            add(l5);
            add(l3);
            add(l2);
            add(l10);

            add(new JLabel("Spawning properties"));
            add(l4);
            add(l11);
            add(new JLabel("Others"));
            add(l1);

            add(buttonPanel);

        }

        @Override
        public void actionPerformed(ActionEvent e) {


            EvolutionSimulatorMap map1 = new EvolutionSimulatorMap(
                    Integer.parseInt(mapWidth.getText()),
                    Integer.parseInt(mapHeight.getText()),
                    Integer.parseInt(jungleRatio.getText()) / 100.0,
                    Integer.parseInt(animalsStartEnergy.getText()),
                    Integer.parseInt(dayEnergyCost.getText()),
                    Integer.parseInt(grassEatingEnergyProfit.getText())
            );
            EvolutionSimulatorMap map2 = new EvolutionSimulatorMap(
                    Integer.parseInt(mapWidth.getText()),
                    Integer.parseInt(mapHeight.getText()),
                    Integer.parseInt(jungleRatio.getText()) / 100.0,
                    Integer.parseInt(animalsStartEnergy.getText()),
                    Integer.parseInt(dayEnergyCost.getText()),
                    Integer.parseInt(grassEatingEnergyProfit.getText())
            );
            MapSimulation simulation = new MapSimulation(
                    map1, Integer.parseInt(delay.getText()),
                    Integer.parseInt(numOfSpawnedAnimals.getText()));
            simulation.frame.setLocation(0,0);
            simulation.startSimulation();
            MapSimulation simulation2 = new MapSimulation(map2, Integer.parseInt(delay.getText()),Integer.parseInt(numOfSpawnedAnimals.getText()));
            simulation2.frame.setLocation(700,0);
            simulation2.startSimulation();

        }
    }

