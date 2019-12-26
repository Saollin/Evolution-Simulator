package pl.obiektowe.projekt1.simulator.Visualization;

import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
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
import javafx.scene.shape.*;
import javafx.util.Duration;
import pl.obiektowe.projekt1.simulator.Classes.Animal;
import pl.obiektowe.projekt1.simulator.Classes.EvolutionSimulatorMap;
import pl.obiektowe.projekt1.simulator.Classes.Plant;
import pl.obiektowe.projekt1.simulator.Classes.Vector2d;

import java.util.*;

public class EvolutionSimulatorController {

    private double height = 500;
    private double width = 500;
    private int rows;
    private int columns;
    private double gridWidth;
    private double gridHeight;
    static boolean alreadyExecuted = false;
    private HashMap<Vector2d, LinkedList<Node>> myNodesMap1 = new HashMap<>();
    private HashMap<Vector2d, LinkedList<Node>> myNodesMap2 = new HashMap<>();
    private HashMap<Animal, Vector2d> previousAnimalsPositions;


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
        EvolutionSimulatorMap esm1 = MenuController.map1;
        EvolutionSimulatorMap esm2 = MenuController.map2;
        rows = esm1.getHeight();
        columns = esm1.getWidth();
        gridWidth = width / rows;
        gridHeight = height / columns;
        drawSteppeAndJungle(MenuController.map1);
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(MenuController.delay),
                new EventHandler<javafx.event.ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent actionEvent) {
                        LinkedList<Plant> plants1 = new LinkedList<>(esm1.getPlants().values());
                        LinkedList<Plant> plants2 = new LinkedList<>(esm2.getPlants().values());
                        if (!alreadyExecuted) {
                            drawAnimalsAndPlantsOnMap1(esm1);
                            alreadyExecuted = true;
                        }
                        if(!esm1.isOnlyOneAnimal()){
//                            removePreviousAnimalsAndPlantsFromMap1(esm1);
                            getPreviousAnimalsPositions(esm1);
                            esm1.oneDay();
                            animateAnimalsOnMap1();
//                            removePreviousAnimalsAndPlantsFromMap1(esm1);
//                            drawAnimalsAndPlantsOnMap1(esm1);
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
                FieldNode node1 = new FieldNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight, color);
                FieldNode node2 = new FieldNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight, color);
                // add node to group
                anchorPaneMap1.getChildren().add(node1);
                anchorPaneMap2.getChildren().add(node2);

                myNodesMap1.put(new Vector2d(i,j), new LinkedList<Node>(Arrays.asList(node1)));
                myNodesMap2.put(new Vector2d(i,j), new LinkedList<Node>(Arrays.asList(node2)));
            }
        }
        //drawing Jungle
        for( int i= map.getJungleLowerLeft().getX(); i < map.getJungleLowerLeft().getX() + map.getJungleWidth(); i++) {
            for( int j=map.getJungleLowerLeft().getY(); j < map.getJungleLowerLeft().getY() + map.getJungleHeight(); j++) {
                Color color = Color.web("rgb(16, 70, 19)");
//                        new Color(148, 171, 48, 1);
                FieldNode node1 = new FieldNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight, color);
                FieldNode node2 = new FieldNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight, color);
                Vector2d position = new Vector2d(i,j);
                myNodesMap1.get(position).add(node1);
                // add node to group
                anchorPaneMap1.getChildren().add(node1);

                myNodesMap2.get(position).add(node2);
                // add node to group
                anchorPaneMap2.getChildren().add(node2);
            }
        }
    }

    public void getPreviousAnimalsPositions(EvolutionSimulatorMap map1){
        HashMap<Animal, Vector2d> result = new HashMap<>();
        for(Animal animal:map1.getAnimalList()){
            result.put(animal, animal.getPosition());
        }
        previousAnimalsPositions = result;
    }

    public void animateAnimalsOnMap1(){
        Iterator it = previousAnimalsPositions.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();
            Animal animal = (Animal) pair.getKey();
            Vector2d newPosition = animal.getPosition();
            Vector2d oldPosition = (Vector2d) pair.getValue();
            Node animalNode = myNodesMap1.get(oldPosition).getLast();
            animalNode.toFront();

            Path path = new Path();
            path.getElements().add (new MoveTo( animalNode.getTranslateX() + animalNode.getBoundsInParent().getWidth() / 2.0, animalNode.getTranslateY() + animalNode.getBoundsInParent().getHeight() / 2.0));
            path.getElements().add (new LineTo( newPosition.getX() * gridWidth + gridWidth / 2, newPosition.getY() * gridHeight + gridHeight / 2));

            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(MenuController.delay * 0.8));
            pathTransition.setNode(animalNode);
            pathTransition.setPath(path);
            myNodesMap1.get(oldPosition).remove(animalNode);
            myNodesMap1.get(newPosition).addLast(animalNode);

            pathTransition.play();
//            animalNode.setTranslateX(newPosition.getX() * gridWidth);
//            animalNode.setTranslateY(newPosition.getY() * gridHeight);
        }


    }

    public void drawAnimalsAndPlantsOnMap1(EvolutionSimulatorMap map1){
        double gridWidth = width / rows;
        double gridHeight = height / columns;
        ArrayList<Plant> plants1 = new ArrayList<>(map1.getPlants().values());
        for(Plant plant:plants1) {
            int i = plant.getPosition().getX();
            int j = plant.getPosition().getY();
            PlantNode plantNode = new PlantNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight);
            Vector2d position = new Vector2d(i,j);
            myNodesMap1.get(position).add(plantNode);
            anchorPaneMap1.getChildren().add(plantNode);
            plantNode.toFront();
        }
        for(Animal animal:map1.getAnimalList()) {
            Color color = map1.colorForAnimal(animal);
            int i = animal.getPosition().getX();
            int j = animal.getPosition().getY();
            AnimalNode node = new AnimalNode(i * gridWidth, j * gridHeight, gridWidth /3, gridHeight /3 , color);
            Vector2d position = new Vector2d(i,j);
            myNodesMap1.get(position).add(node);
            anchorPaneMap1.getChildren().add(node);
        }
    }

    public void drawAnimalsAndPlantsOnMap2(EvolutionSimulatorMap map2){
        ArrayList<Plant> plants2 = new ArrayList<>(map2.getPlants().values());
        for(Plant plant:plants2) {
            int i = plant.getPosition().getX();
            int j = plant.getPosition().getY();
            PlantNode plantNode = new PlantNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight);
            Vector2d position = new Vector2d(i,j);
            myNodesMap2.get(position).add(plantNode);
            anchorPaneMap2.getChildren().add(plantNode);
            plantNode.toFront();
        }
        for(Animal animal:map2.getAnimalList()) {
            Color color = map2.colorForAnimal(animal);
            int i = animal.getPosition().getX();
            int j = animal.getPosition().getY();
            AnimalNode node = new AnimalNode(i * gridWidth, j * gridHeight, gridWidth /3, gridHeight /3 , color);
            Vector2d position = new Vector2d(i,j);
            myNodesMap2.get(position).add(node);
            anchorPaneMap2.getChildren().add(node);
        }
    }

    public void removePreviousAnimalsAndPlantsFromMap1(EvolutionSimulatorMap map1){
        LinkedList<Plant> plants1 = new LinkedList<>(map1.getPlants().values());
        LinkedList<Vector2d> animalsPosition1 = new LinkedList<>(previousAnimalsPositions.values());
        for(Vector2d position:animalsPosition1){
//            Vector2d position = animal.getPosition();
            AnimalNode animalNode;
            LinkedList<Node> nodes = new LinkedList<>(myNodesMap1.get(position));
            for(Node node:nodes){
                if(node instanceof AnimalNode){
                    animalNode = (AnimalNode) node;
                    myNodesMap1.get(position).remove(animalNode);
                    anchorPaneMap1.getChildren().remove(animalNode);
                }
            }
        }
        for(Plant plant:plants1){
//            int i = plant.getPosition().getX();
//            int j = plant.getPosition().getY();
            Vector2d position = plant.getPosition();
            PlantNode plantNode;
            LinkedList<Node> nodes = new LinkedList<>(myNodesMap1.get(position));
            for(Node node:nodes){
                if(node instanceof PlantNode){
                    plantNode = (PlantNode) node;
                    myNodesMap1.get(position).remove(plantNode);
                    anchorPaneMap1.getChildren().remove(plantNode);
                }
            }
        }
    }

    public void removePreviousAnimalsAndPlantsFromMap2(EvolutionSimulatorMap map2){
        LinkedList<Plant> plants2 = new LinkedList<>(map2.getPlants().values());
        LinkedList<Animal> animals2 = new LinkedList<>(map2.getAnimalList());
        for(Animal animal:animals2){
//            int i = animal.getPosition().getX();
//            int j = animal.getPosition().getY();
            Vector2d position = animal.getPosition();
            AnimalNode animalNode;
            LinkedList<Node> nodes = new LinkedList<>(myNodesMap2.get(position));
            for(Node node:nodes){
                if(node instanceof AnimalNode){
                    animalNode = (AnimalNode) node;
                    myNodesMap2.get(position).remove(animalNode);
                    anchorPaneMap2.getChildren().remove(animalNode);
                }
            }
        }
        for(Plant plant:plants2){
//            int i = plant.getPosition().getX();
//            int j = plant.getPosition().getY();
            Vector2d position = plant.getPosition();
            PlantNode plantNode;
            LinkedList<Node> nodes = new LinkedList<>(myNodesMap2.get(position));
            for(Node node:nodes){
                if(node instanceof PlantNode){
                    plantNode = (PlantNode) node;
                    myNodesMap2.get(position).remove(plantNode);
                    anchorPaneMap2.getChildren().remove(plantNode);
                }
            }
        }
    }

    public static class FieldNode extends StackPane {

        public FieldNode(double x, double y, double width, double height, Color color) {

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

    public static class AnimalNode extends StackPane {

        public AnimalNode(double x, double y, double width, double height, Color color) {
            double radius = width < height ? width : height;
            Circle circle = new Circle(radius,color);

            setTranslateX(x + width/2);
            setTranslateY(y + height/2);

            getChildren().addAll(circle);
        }
    }

    public class PlantNode extends ImageView {

        private Image image = new Image(String.valueOf(getClass().getClassLoader().getResource("plant2.png")));

            PlantNode(double x, double y, double width, double height){
                super();
                this.setImage(image);
                this.setFitWidth(width);
                this.setFitHeight(height);
                setTranslateX(x);
                setTranslateY(y);
            }

    }

}
