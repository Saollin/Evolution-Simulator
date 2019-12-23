package pl.obiektowe.projekt1.simulator.Visualization;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.obiektowe.projekt1.simulator.DataModel.StartParameters;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
        root.requestFocus();
        primaryStage.setTitle("Evolution Simulator - settings menu");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        try {
            StartParameters.getInstance().storeStartParameters();

        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void init() throws Exception {
        try {
            StartParameters.getInstance().loadStartParameters();

        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
