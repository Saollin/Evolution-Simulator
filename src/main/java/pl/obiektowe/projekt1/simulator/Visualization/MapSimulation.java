package pl.obiektowe.projekt1.simulator.Visualization;

import pl.obiektowe.projekt1.simulator.Classes.EvolutionSimulator;
import pl.obiektowe.projekt1.simulator.Classes.EvolutionSimulatorMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MapSimulation implements ActionListener {

    //simulation options:
    public final int delay;
    public EvolutionSimulatorMap map;
    public int startNumOfAnimals;
    public int grassSpawnedInEachDay;

    //simulation necessary:
    public JFrame frame;
    public RenderPanel renderPanel;
    public PlotRenderPanel plotRenderPanel;
    public Timer timer;


    public MapSimulation(EvolutionSimulatorMap map, int delay, int startNumOfAnimals) {

        this.map = map;
        this.delay = delay;
        this.startNumOfAnimals = startNumOfAnimals;

        timer = new Timer(delay, this);

        frame = new JFrame("Evolution Simulator");
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        renderPanel = new RenderPanel(map, this);
        renderPanel.setSize(new Dimension(1, 1));

        plotRenderPanel = new PlotRenderPanel(map, this);
        plotRenderPanel.setSize(1, 1);

        frame.add(renderPanel);
        frame.add(plotRenderPanel);

    }

    public void startSimulation() {

        for (int i = 0; i < startNumOfAnimals; i++) {
            map.placeAnimalInRandomFieldInJungle();
        }
        timer.start();

    }

    @Override
    //It will executed when timer finish Counted
    public void actionPerformed(ActionEvent e) {
        plotRenderPanel.repaint();
        renderPanel.repaint();

        map.oneDay();

    }

}