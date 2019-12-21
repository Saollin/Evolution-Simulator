package pl.obiektowe.projekt1.simulator.Visualization;

import javax.swing.*;

public class Menu {
    public JFrame menuFrame;

    public Menu(Integer[] defaultMapProperties) {

        menuFrame = new JFrame("Evolution Simulator (Settings)");
        menuFrame.setSize(500, 500);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.add(new SettingsPanel(defaultMapProperties));
        menuFrame.setVisible(true);
    }

    public static void main(String[] args) {

        try {

            Integer[] defaultMapProperties = {20, 20, 40, 30, 2, 6, 50, 10};
            Menu menu = new Menu(defaultMapProperties);

        } catch (IllegalArgumentException ex) {
            System.out.println(ex);
            return;
        }


    }
}

