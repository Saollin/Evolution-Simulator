package pl.obiektowe.projekt1.simulator.Visualization;

import pl.obiektowe.projekt1.simulator.Classes.Animal;
import pl.obiektowe.projekt1.simulator.Classes.EvolutionSimulatorMap;
import pl.obiektowe.projekt1.simulator.Classes.Plant;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RenderPanel extends JPanel {

    public EvolutionSimulatorMap map;
    public MapSimulation simulation;

    public RenderPanel(EvolutionSimulatorMap map, MapSimulation simulation) {
        this.map = map;
        this.simulation = simulation;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setSize((int) (simulation.frame.getWidth() * 0.6), simulation.frame.getHeight() - 38);
        this.setLocation((int) (0.4 * simulation.frame.getWidth()), 0);
        int width = this.getWidth();
        int height = this.getHeight(); //38 is toolbar size
        int widthScale = Math.round(width / map.getWidth());
        int heightScale = height / map.getHeight();

        //draw Steppe
        g.setColor(new Color(148, 171, 48));
        g.fillRect(0, 0, width, height);

        //draw Jungle
        g.setColor(new Color(16, 70, 19));
        g.fillRect(map.getJungleLowerLeft().getX() * widthScale,
                map.getJungleLowerLeft().getY() * heightScale,
                map.getJungleWidth() * widthScale,
                map.getJungleHeight() * heightScale);

        //draw Grass
        ArrayList<Plant> plants = new ArrayList<>(map.getPlants().values());
        for (Plant plant : plants) {
            g.setColor(plant.toColor());
            int y = map.countRightPositionOnTheMap(plant.getPosition()).getY() * heightScale;
            int x = map.countRightPositionOnTheMap(plant.getPosition()).getX() * widthScale;
            g.fillRect(x, y, widthScale, heightScale);
        }
        //draw Animals
//        for (Animal a : map.getAnimalList()) {
//            g.setColor(map.colorForAnimal(a));
//            int y = map.countRightPositionOnTheMap(a.getPosition()).getY() * heightScale;
//            int x = map.countRightPositionOnTheMap(a.getPosition()).getX() * widthScale;
//            g.fillOval(x, y, widthScale, heightScale);
//        }
    }

}

