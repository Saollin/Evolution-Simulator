module EvolutionSimulator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires json.simple;

    opens pl.obiektowe.projekt1.simulator.Visualization;
    opens pl.obiektowe.projekt1.simulator.DataModel;
}