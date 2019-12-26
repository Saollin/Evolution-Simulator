package pl.obiektowe.projekt1.simulator.Visualization;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pl.obiektowe.projekt1.simulator.Classes.EvolutionSimulator;
import pl.obiektowe.projekt1.simulator.Classes.EvolutionSimulatorMap;
import pl.obiektowe.projekt1.simulator.DataModel.StartParameters;

import java.io.IOException;

public class MenuController {

    @FXML
    private TextField width;

    @FXML
    private TextField height;

    @FXML
    private TextField jungleRatio;

    @FXML
    private TextField moveEnergy;

    @FXML
    private TextField startEnergy;

    @FXML
    private TextField plantEnergy;

    @FXML
    private TextField numberOfAnimalsToSpawn;

    @FXML
    private TextField refreshingTime;

    @FXML
    private Button startButton;

    public static EvolutionSimulatorMap map1;
    public static EvolutionSimulatorMap map2;
    public static int delay;

    public void initialize(){
        StartParameters startParameters = StartParameters.getInstance();
        width.setText(startParameters.getWidth()+"");
        height.setText(startParameters.getHeight()+"");
        jungleRatio.setText(startParameters.getJungleRatio()+"");
        moveEnergy.setText(startParameters.getMoveEnergy()+"");
        startEnergy.setText(startParameters.getStartEnergy()+"");
        plantEnergy.setText(startParameters.getPlantEnergy()+"");
        numberOfAnimalsToSpawn.setText(startParameters.getNumberOfAnimalsToSpawn()+"");
        refreshingTime.setText(startParameters.getRefreshingTime()+"");
        width.setOnKeyPressed(new KeyHandle());
        height.setOnKeyPressed(new KeyHandle());
        jungleRatio.setOnKeyPressed(new KeyHandle());
        moveEnergy.setOnKeyPressed(new KeyHandle());
        startEnergy.setOnKeyPressed(new KeyHandle());
        plantEnergy.setOnKeyPressed(new KeyHandle());
        numberOfAnimalsToSpawn.setOnKeyPressed(new KeyHandle());
        refreshingTime.setOnKeyPressed(new KeyHandle());
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Parent root;
                try {
                    map1 = new EvolutionSimulatorMap(startParameters);
                    map2 = new EvolutionSimulatorMap(startParameters);
                    delay = startParameters.getRefreshingTime();
                    for(int i = 0; i < startParameters.getNumberOfAnimalsToSpawn(); i++){
                        map1.placeAnimalInRandomFieldInJungle();
                        map2.placeAnimalInRandomFieldInJungle();
                    }
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EvolutionSimulator.fxml"));
                    root = (Parent) loader.load();
                    EvolutionSimulatorController controller = loader.getController();
                    Stage stage = new Stage();
                    stage.setMaximized(true);
                    stage.setTitle("My New Stage Title");
                    stage.setScene(new Scene(root));
                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent windowEvent) {
                            controller.timeline.stop();
                        }
                    });
                    stage.show();

                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class KeyHandle implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent keyEvent) {
            if(keyEvent.getCode().isDigitKey()) {
                TextField textField = (TextField) keyEvent.getSource();
                int keyPressed = Integer.parseInt(keyEvent.getText());
                StartParameters.getInstance().setParameter(Integer.parseInt(textField.getText() + keyPressed),
                        textField.getId());
            }
        }
    }
}
