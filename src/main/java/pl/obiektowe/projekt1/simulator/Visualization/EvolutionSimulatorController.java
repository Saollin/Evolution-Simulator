package pl.obiektowe.projekt1.simulator.Visualization;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.util.Duration;
import pl.obiektowe.projekt1.simulator.Classes.*;

import java.io.IOException;

public class EvolutionSimulatorController {

//    private double height = 500;
//    private double width = 500;
//    private int rows;
//    private int columns;
//    private double gridWidth;
//    private double gridHeight;
//    static boolean alreadyExecuted = false;
//    private HashMap<Vector2d, LinkedList<Node>> myNodesMap1 = new HashMap<>();
//    private HashMap<Vector2d, LinkedList<Node>> myNodesMap2 = new HashMap<>();
//    private HashMap<Animal, Vector2d> previousAnimalsPositions;


    public Timeline timeline;
    public static int whichDay = 0;

    @FXML
    private Button saveButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button startButton;

    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private Label numberOfDay;

    private EvolutionSimulatorMap map1;
    private EvolutionSimulatorMap map2;

    EvolutionSimulatorController(EvolutionSimulatorMap map1, EvolutionSimulatorMap map2){
        this.map1 = map1;
        this.map2 = map2;
    }


    public void initialize() throws IOException {
        FXMLLoader map1Loader = new FXMLLoader();
        FXMLLoader map2Loader = new FXMLLoader();
        map1Loader.setLocation(getClass().getResource("/fxml/MapAndStatistic.fxml"));
        map2Loader.setLocation(getClass().getResource("/fxml/MapAndStatistic.fxml"));
        MapAndStatisticController controllerOfMap1 = new MapAndStatisticController(map1);
        MapAndStatisticController controllerOfMap2 = new MapAndStatisticController(map2);
        map1Loader.setController(controllerOfMap1);
        map2Loader.setController(controllerOfMap2);
        SplitPane pane1 = map1Loader.load();
        SplitPane pane2 = map2Loader.load();
        mainSplitPane.getItems().addAll(pane1,pane2);
        mainSplitPane.setOrientation(Orientation.HORIZONTAL);
        mainSplitPane.setDividerPositions(0.5);
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(MenuController.delay),
                new EventHandler<javafx.event.ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent actionEvent) {
                        whichDay++;
                        numberOfDay.setText(whichDay+"");
                        if(!map1.isOneOrLessAnimal()){
                            controllerOfMap1.animation();
                            controllerOfMap1.setStatistics();
                        }
                        if(!map2.isOneOrLessAnimal()){
                            controllerOfMap2.animation();
                            controllerOfMap2.setStatistics();
                        }
                        if(map1.isOneOrLessAnimal() && map2.isOneOrLessAnimal()){
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
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    map1.statistics.saveAverageStaticAfterGivenNumberOfDay(whichDay);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
//
//    public void drawSteppeAndJungle(EvolutionSimulatorMap map){
//        double gridWidth = width / rows;
//        double gridHeight = height / columns;
//        // initialize steppe
//        for( int i=0; i < rows; i++) {
//            for( int j=0; j < columns; j++) {
//                Color color = Color.web("rgb(148, 171, 48)");
////                        new Color(148, 171, 48, 1);
//                FieldNode node1 = new FieldNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight, color);
//                FieldNode node2 = new FieldNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight, color);
//                // add node to group
//                anchorPaneMap1.getChildren().add(node1);
//                anchorPaneMap2.getChildren().add(node2);
//
//                myNodesMap1.put(new Vector2d(i,j), new LinkedList<Node>(Arrays.asList(node1)));
//                myNodesMap2.put(new Vector2d(i,j), new LinkedList<Node>(Arrays.asList(node2)));
//            }
//        }
//        //drawing Jungle
//        for( int i= map.getJungleLowerLeft().getX(); i < map.getJungleLowerLeft().getX() + map.getJungleWidth(); i++) {
//            for( int j=map.getJungleLowerLeft().getY(); j < map.getJungleLowerLeft().getY() + map.getJungleHeight(); j++) {
//                Color color = Color.web("rgb(16, 70, 19)");
////                        new Color(148, 171, 48, 1);
//                FieldNode node1 = new FieldNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight, color);
//                FieldNode node2 = new FieldNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight, color);
//                Vector2d position = new Vector2d(i,j);
//                myNodesMap1.get(position).add(node1);
//                // add node to group
//                anchorPaneMap1.getChildren().add(node1);
//
//                myNodesMap2.get(position).add(node2);
//                // add node to group
//                anchorPaneMap2.getChildren().add(node2);
//            }
//        }
//    }
//
//    public void getPreviousAnimalsPositions(EvolutionSimulatorMap map1){
//        HashMap<Animal, Vector2d> result = new HashMap<>();
//        for(Animal animal:map1.getAnimalList()){
//            result.put(animal, animal.getPosition());
//        }
//        previousAnimalsPositions = result;
//    }
//
//    public void animateAnimalsOnMap1(EvolutionSimulatorMap map1){
//        Iterator it = previousAnimalsPositions.entrySet().iterator();
//        while(it.hasNext()){
//            Map.Entry pair = (Map.Entry) it.next();
//            Animal animal = (Animal) pair.getKey();
//            Vector2d newPosition = animal.getPosition();
//            Vector2d oldPosition = (Vector2d) pair.getValue();
//            AnimalNode animalNode = (AnimalNode) myNodesMap1.get(oldPosition).getLast();
//            animalNode.toFront();
//            animalNode.changeColorOfAnimal(map1.colorForAnimal(animal));
//
//            Path path = new Path();
//            path.getElements().add (new MoveTo( animalNode.getTranslateX() + animalNode.getBoundsInParent().getWidth() / 2.0, animalNode.getTranslateY() + animalNode.getBoundsInParent().getHeight() / 2.0));
//            path.getElements().add (new LineTo( newPosition.getX() * gridWidth + gridWidth / 2, newPosition.getY() * gridHeight + gridHeight / 2));
//
//            PathTransition pathTransition = new PathTransition();
//            pathTransition.setDuration(Duration.millis(MenuController.delay));
//            pathTransition.setNode(animalNode);
//            pathTransition.setPath(path);
//            myNodesMap1.get(oldPosition).remove(animalNode);
//            myNodesMap1.get(newPosition).addLast(animalNode);
//
//            pathTransition.play();
//        }
//    }
//
//    public void drawAnimalsAndPlantsOnMap1(EvolutionSimulatorMap map1){
//        ArrayList<Plant> plants1 = new ArrayList<>(map1.getPlants().values());
//        for(Plant plant:plants1) {
//            int i = plant.getPosition().getX();
//            int j = plant.getPosition().getY();
//            PlantNode plantNode = new PlantNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight);
//            Vector2d position = new Vector2d(i,j);
//            myNodesMap1.get(position).add(plantNode);
//            anchorPaneMap1.getChildren().add(plantNode);
//            plantNode.toFront();
//        }
//        if(previousAnimalsPositions.size() < map1.getAnimalList().size()) {
//            LinkedList<Animal> newAnimals = new LinkedList<>(map1.getAnimalList());
//            for(Animal existedAnimal: previousAnimalsPositions.keySet()){
//                newAnimals.remove(existedAnimal);
//            }
//            for (Animal newAnimal : newAnimals) {
//                Color color = map1.colorForAnimal(newAnimal);
//                int i = newAnimal.getPosition().getX();
//                int j = newAnimal.getPosition().getY();
//                AnimalNode node = new AnimalNode(i * gridWidth, j * gridHeight, gridWidth / 3, gridHeight / 3, color);
//                Vector2d position = new Vector2d(i, j);
//                myNodesMap1.get(position).add(node);
//                anchorPaneMap1.getChildren().add(node);
//            }
//        }
//    }
//    public void removePreviousAnimalsFromMap1(EvolutionSimulatorMap map1){
//        if (previousAnimalsPositions.size() > map1.getAnimalList().size()) {
//            LinkedList<Animal> deadAnimals = new LinkedList<>(previousAnimalsPositions.keySet());
//            //remove living animals from list of death ones
//            for(Animal livingAnimal:map1.getAnimalList()){
//                deadAnimals.remove(livingAnimal);
//            }
//            for(Animal deadAnimal:deadAnimals){
//                Vector2d position = deadAnimal.getPosition();
//                AnimalNode animalNode;
//                LinkedList<Node> nodes = new LinkedList<>(myNodesMap1.get(position));
//                for(Node node:nodes){
//                    if(node instanceof AnimalNode){
//                        animalNode = (AnimalNode) node;
//                        myNodesMap1.get(position).remove(animalNode);
//                        anchorPaneMap1.getChildren().remove(animalNode);
//                    }
//                }
//            }
//        }
//    }
//    public void removePreviousPlantsFromMap1(EvolutionSimulatorMap map1){
//        LinkedList<Plant> plants1 = new LinkedList<>(map1.getPlants().values());
//        for(Plant plant:plants1){
////            int i = plant.getPosition().getX();
////            int j = plant.getPosition().getY();
//            Vector2d position = plant.getPosition();
//            PlantNode plantNode;
//            LinkedList<Node> nodes = new LinkedList<>(myNodesMap1.get(position));
//            for(Node node:nodes){
//                if(node instanceof PlantNode){
//                    plantNode = (PlantNode) node;
//                    myNodesMap1.get(position).remove(plantNode);
//                    anchorPaneMap1.getChildren().remove(plantNode);
//                }
//            }
//        }
//    }
//
//    public void drawAnimalsAndPlantsOnMap2(EvolutionSimulatorMap map2){
//        ArrayList<Plant> plants2 = new ArrayList<>(map2.getPlants().values());
//        for(Plant plant:plants2) {
//            int i = plant.getPosition().getX();
//            int j = plant.getPosition().getY();
//            PlantNode plantNode = new PlantNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight);
//            Vector2d position = new Vector2d(i,j);
//            myNodesMap2.get(position).add(plantNode);
//            anchorPaneMap2.getChildren().add(plantNode);
//            plantNode.toFront();
//        }
//        for(Animal animal:map2.getAnimalList()) {
//            Color color = map2.colorForAnimal(animal);
//            int i = animal.getPosition().getX();
//            int j = animal.getPosition().getY();
//            AnimalNode node = new AnimalNode(i * gridWidth, j * gridHeight, gridWidth /3, gridHeight /3 , color);
//            Vector2d position = new Vector2d(i,j);
//            myNodesMap2.get(position).add(node);
//            anchorPaneMap2.getChildren().add(node);
//        }
//    }
//
//
//
//    public void removePreviousAnimalsAndPlantsFromMap2(EvolutionSimulatorMap map2){
//        LinkedList<Plant> plants2 = new LinkedList<>(map2.getPlants().values());
//        LinkedList<Animal> animals2 = new LinkedList<>(map2.getAnimalList());
//        for(Animal animal:animals2){
////            int i = animal.getPosition().getX();
////            int j = animal.getPosition().getY();
//            Vector2d position = animal.getPosition();
//            AnimalNode animalNode;
//            LinkedList<Node> nodes = new LinkedList<>(myNodesMap2.get(position));
//            for(Node node:nodes){
//                if(node instanceof AnimalNode){
//                    animalNode = (AnimalNode) node;
//                    myNodesMap2.get(position).remove(animalNode);
//                    anchorPaneMap2.getChildren().remove(animalNode);
//                }
//            }
//        }
//        for(Plant plant:plants2){
////            int i = plant.getPosition().getX();
////            int j = plant.getPosition().getY();
//            Vector2d position = plant.getPosition();
//            PlantNode plantNode;
//            LinkedList<Node> nodes = new LinkedList<>(myNodesMap2.get(position));
//            for(Node node:nodes){
//                if(node instanceof PlantNode){
//                    plantNode = (PlantNode) node;
//                    myNodesMap2.get(position).remove(plantNode);
//                    anchorPaneMap2.getChildren().remove(plantNode);
//                }
//            }
//        }
//    }
//
//    public static class FieldNode extends StackPane {
//
//        public FieldNode(double x, double y, double width, double height, Color color) {
//
//            // create rectangle
//            Rectangle rectangle = new Rectangle(width, height);
//            rectangle.setStroke(Color.BLACK);
//            rectangle.setStrokeWidth(0.1);
//            rectangle.setFill(color);
//
//            // set position
//            setTranslateX(x);
//            setTranslateY(y);
//
//            getChildren().addAll(rectangle);
//
//        }
//    }
//
//    public static class AnimalNode extends StackPane {
//
//        public AnimalNode(double x, double y, double width, double height, Color color) {
//            double radius = width < height ? width : height;
//            Circle circle = new Circle(radius,color);
//
//            setTranslateX(x + width/2);
//            setTranslateY(y + height/2);
//
//            getChildren().addAll(circle);
//        }
//
//        public void changeColorOfAnimal(Color color){
//            Circle circle = (Circle) getChildren().get(0);
//            circle.setFill(color);
//        }
//    }
//
//    public class PlantNode extends ImageView {
//
//        private Image image = new Image(String.valueOf(getClass().getClassLoader().getResource("plant2.png")));
//
//            PlantNode(double x, double y, double width, double height){
//                super();
//                this.setImage(image);
//                this.setFitWidth(width);
//                this.setFitHeight(height);
//                setTranslateX(x);
//                setTranslateY(y);
//            }
//
//    }

}
