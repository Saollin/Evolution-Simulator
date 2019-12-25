package pl.obiektowe.projekt1.simulator.Visualization;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import pl.obiektowe.projekt1.simulator.Classes.Animal;
import pl.obiektowe.projekt1.simulator.Classes.EvolutionSimulatorMap;
import pl.obiektowe.projekt1.simulator.Classes.Plant;

import java.util.ArrayList;
import java.util.LinkedList;

public class EvolutionSimulatorController {
    private double height = 500;
    private double width = 500;
    private int rows;
    private int columns;


    public Timeline timeline;

    @FXML
    private Button saveButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button startButton;

    //map1
    @FXML
    private SplitPane map1;
    @FXML
    public AnchorPane anchorPaneMap1;
    @FXML
    private Label numberOfAnimals1;
    @FXML
    private Label averageEnergyOfLivingAnimals1;
    @FXML
    private Label numberOfPlants1;
    @FXML
    private Label averageLifetimeOfDeadAnimals1;
    @FXML
    private Label averageNumberChildOfLivingAnimals1;
    @FXML
    private Label dominantGenotype1;

    private StackPane [][] myNodesMap1;
    private StackPane [][] myNodesMap2;

    //disactivated map1
    @FXML
    private Label chosenAnimal1;
    @FXML
    private Label numberOfChildLabel1;
    @FXML
    private Label numberOfChild1;
    @FXML
    private Label numberOfDescendantsLabel1;
    @FXML
    private Label numberOfDescendants1;
    @FXML
    private Label dayOfDieLabel1;
    @FXML
    private Label dayOfDie1;

    @FXML
    private SplitPane map2;
    @FXML
    public AnchorPane anchorPaneMap2;
    @FXML
    private Label numberOfAnimals2;
    @FXML
    private Label averageEnergyOfLivingAnimals2;
    @FXML
    private Label numberOfPlants2;
    @FXML
    private Label averageLifetimeOfDeadAnimals2;
    @FXML
    private Label averageNumberChildOfLivingAnimals2;
    @FXML
    private Label dominantGenotype2;

    //disactivated map2
    @FXML
    private Label chosenAnimal2;
    @FXML
    private Label numberOfChildLabel2;
    @FXML
    private Label numberOfChild2;
    @FXML
    private Label numberOfDescendantsLabel2;
    @FXML
    private Label numberOfDescendants2;
    @FXML
    private Label dayOfDieLabel2;
    @FXML
    private Label dayOfDie2;


    public void initialize(){
        rows = MenuController.map1.getHeight();
        columns = MenuController.map1.getWidth();
        myNodesMap1 = new StackPane[rows][columns];
        myNodesMap2 = new StackPane[rows][columns];
        drawSteppeAndJungle(MenuController.map1);
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(MenuController.delay),
                new EventHandler<javafx.event.ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent actionEvent) {
                        EvolutionSimulatorMap esm1 = MenuController.map1;
                        EvolutionSimulatorMap esm2 = MenuController.map2;
                        LinkedList<Plant> plants1 = new LinkedList<>(esm1.getPlants().values());
                        LinkedList<Plant> plants2 = new LinkedList<>(esm2.getPlants().values());
                        if(!esm1.isOnlyOneAnimal()){
                            removePreviousAnimalsAndPlantsFromMap1(esm1);
                            esm1.oneDay();
                            drawAnimalsAndPlantsOnMap1(esm1);
                        }
                        if(!esm2.isOnlyOneAnimal()) {
                            removePreviousAnimalsAndPlantsFromMap2(esm2);
                            esm2.oneDay();
                            drawAnimalsAndPlantsOnMap2(esm2);
                        }
                        if(esm1.isOnlyOneAnimal() && esm2.isOnlyOneAnimal()){
                            timeline.stop();
                        }
                        }
                }));
        timeline.playFromStart();
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                    timeline.stop();
            }
        });
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                timeline.play();
            }
        });
    }

//    @Override
//    public void actionPerformed(ActionEvent e) {
//        EvolutionSimulatorMap esm1 = MenuController.map1;
//        EvolutionSimulatorMap esm2 = MenuController.map2;
//        LinkedList<Plant> plants1 = new LinkedList<>(esm1.getPlants().values());
//        LinkedList<Plant> plants2 = new LinkedList<>(esm2.getPlants().values());
//        removerPreviousAnimalsAndPlants(esm1.getAnimalList(),plants1,anchorPaneMap1, myNodesMap1);
//        removerPreviousAnimalsAndPlants(esm2.getAnimalList(),plants2,anchorPaneMap2, myNodesMap2);
//        esm1.oneDay();
//        esm2.oneDay();
//        drawAnimalsAndPlants(esm1,anchorPaneMap1,myNodesMap1);
//        drawAnimalsAndPlants(esm2,anchorPaneMap2,myNodesMap2);
//    }

    public void drawSteppeAndJungle(EvolutionSimulatorMap map){
        double gridWidth = width / rows;
        double gridHeight = height / columns;
        // initialize steppe
        for( int i=0; i < rows; i++) {
            for( int j=0; j < columns; j++) {
                Color color = Color.web("rgb(148, 171, 48)");
//                        new Color(148, 171, 48, 1);
                MyNode node1 = new MyNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight, color);
                MyNode node2 = new MyNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight, color);
                // add node to group
                anchorPaneMap1.getChildren().add(node1);
                anchorPaneMap2.getChildren().add(node2);

                myNodesMap1[i][j] = node1;
                myNodesMap2[i][j] = node2;
            }
        }
        //drawing Jungle
        for( int i= map.getJungleLowerLeft().getX(); i < map.getJungleLowerLeft().getX() + map.getJungleWidth(); i++) {
            for( int j=map.getJungleLowerLeft().getY(); j < map.getJungleLowerLeft().getY() + map.getJungleHeight(); j++) {
                Color color = Color.web("rgb(16, 70, 19)");
//                        new Color(148, 171, 48, 1);
                MyNode node1 = new MyNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight, color);
                MyNode node2 = new MyNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight, color);
                StackPane oldNode = myNodesMap1[i][j];
                myNodesMap1[i][j] = node1;
                // add node to group
                anchorPaneMap1.getChildren().remove(oldNode);
                anchorPaneMap1.getChildren().add(node1);

                myNodesMap2[i][j] = node2;
                // add node to group
                anchorPaneMap2.getChildren().remove(oldNode);
                anchorPaneMap2.getChildren().add(node2);
            }
        }
    }

    public void drawAnimalsAndPlantsOnMap1(EvolutionSimulatorMap map1){
        double gridWidth = width / rows;
        double gridHeight = height / columns;
        ArrayList<Plant> plants1 = new ArrayList<>(map1.getPlants().values());
        for(Plant plant:plants1) {
            Color color = Color.web("rgb(63, 179, 42)");
//            Image image = new Image(String.valueOf(getClass().getClassLoader().getResource("plant.png")));
//            ImageView imageView = new ImageView();
//            imageView.setImage(image);
            int i = plant.getPosition().getX();
            int j = plant.getPosition().getY();
            MyNode node = new MyNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight , color);
            StackPane oldNode = myNodesMap1[i][j];
            oldNode.getChildren().add(node);
            // add node to group
            anchorPaneMap1.getChildren().add(node);
        }
        for(Animal animal:map1.getAnimalList()) {
            Color color = map1.colorForAnimal(animal);
            int i = animal.getPosition().getX();
            int j = animal.getPosition().getY();
            OvalNode node = new OvalNode(i * gridWidth, j * gridHeight, gridWidth /3, gridHeight /3 , color);
            StackPane oldNode = myNodesMap1[i][j];
            oldNode.getChildren().add(node);
            // add node to group
            anchorPaneMap1.getChildren().add(node);
        }
    }
    public void drawAnimalsAndPlantsOnMap2(EvolutionSimulatorMap map2){
        double gridWidth = width / rows;
        double gridHeight = height / columns;
        ArrayList<Plant> plants2 = new ArrayList<>(map2.getPlants().values());
        for(Plant plant:plants2) {
//            Color color = Color.web("rgb(63, 179, 42)");
            Image image = new Image(String.valueOf(getClass().getClassLoader().getResource("plant.png")));
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            int i = plant.getPosition().getX();
            int j = plant.getPosition().getY();
//            MyNode node = new MyNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight , color);
            imageView.setFitWidth(gridWidth);
            imageView.setFitHeight(gridHeight);
            StackPane oldNode = myNodesMap2[i][j];
            oldNode.getChildren().add(imageView);
            // add node to group
            anchorPaneMap2.getChildren().add(imageView);
            imageView.toFront();
        }
        for(Animal animal:map2.getAnimalList()) {
            Color color = map2.colorForAnimal(animal);
            int i = animal.getPosition().getX();
            int j = animal.getPosition().getY();
            OvalNode node = new OvalNode(i * gridWidth, j * gridHeight, gridWidth /3, gridHeight /3 , color);
            StackPane oldNode = myNodesMap2[i][j];
            oldNode.getChildren().add(node);
            // add node to group
            anchorPaneMap2.getChildren().add(node);
        }
    }

    public void removePreviousAnimalsAndPlantsFromMap1(EvolutionSimulatorMap map1){
        LinkedList<Plant> plants1 = new LinkedList<>(map1.getPlants().values());
        LinkedList<Animal> animals1 = new LinkedList<>(map1.getAnimalList());
        for(Animal animal:animals1){
            int i = animal.getPosition().getX();
            int j = animal.getPosition().getY();
            OvalNode animalNode;
            for(Node node:myNodesMap1[i][j].getChildren()){
                if(node instanceof OvalNode){
                    animalNode = (OvalNode) node;
                    myNodesMap1[i][j].getChildren().remove(node);
                    anchorPaneMap1.getChildren().remove(node);
                }
            }
        }
        for(Plant plant:plants1){
            int i = plant.getPosition().getX();
            int j = plant.getPosition().getY();
            OvalNode plantNode;
            for(Node node:myNodesMap1[i][j].getChildren()){
                if(node instanceof OvalNode){
                    plantNode = (OvalNode) node;
                    myNodesMap1[i][j].getChildren().remove(node);
                    anchorPaneMap1.getChildren().remove(node);
                }
            }
        }
    }

    public void removePreviousAnimalsAndPlantsFromMap2(EvolutionSimulatorMap map2){
        LinkedList<Plant> plants2 = new LinkedList<>(map2.getPlants().values());
        LinkedList<Animal> animals2 = new LinkedList<>(map2.getAnimalList());
        for(Animal animal:animals2){
            int i = animal.getPosition().getX();
            int j = animal.getPosition().getY();
            OvalNode animalNode;
            ArrayList<Node> nodes = new ArrayList<>(myNodesMap2[i][j].getChildren());
            for(Node node:nodes){
                if(node instanceof OvalNode){
                    animalNode = (OvalNode) node;
                    myNodesMap2[i][j].getChildren().remove(node);
                    anchorPaneMap2.getChildren().remove(node);
                }
            }
        }
        for(Plant plant:plants2){
            int i = plant.getPosition().getX();
            int j = plant.getPosition().getY();
            OvalNode plantNode;
            for(Node node:myNodesMap2[i][j].getChildren()){
                if(node instanceof OvalNode){
                    plantNode = (OvalNode) node;
                    myNodesMap2[i][j].getChildren().remove(node);
                    anchorPaneMap2.getChildren().remove(node);
                }
            }
        }
    }

    public static class MyNode extends StackPane {

        public MyNode(double x, double y, double width, double height, Color color) {

            // create rectangle
            Rectangle rectangle = new Rectangle(width, height);
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(0.1);
            rectangle.setFill(color);

            // set position
            setTranslateX(x);
            setTranslateY(y);

            getChildren().addAll(rectangle);

        }
    }

    public static class OvalNode extends StackPane {

        public OvalNode(double x, double y, double width, double height, Color color) {
            double radius = width < height ? width : height;
            Circle circle = new Circle(radius,color);

            setTranslateX(x + width/2);
            setTranslateY(y + height/2);

            getChildren().addAll(circle);
        }
    }

}
