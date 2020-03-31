package pl.obiektowe.projekt1.simulator.Visualization;

import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
import pl.obiektowe.projekt1.simulator.DataModel.Log;
import pl.obiektowe.projekt1.simulator.DataModel.StatisticOfDay;

import java.util.*;

public class MapAndStatisticController {
    private double height = 500;
    private double width = 500;
    private int rows;
    private int columns;
    private double gridWidth;
    private double gridHeight;
    boolean alreadyExecuted = false;
    private HashMap<Vector2d, LinkedList<Node>> myNodesMap = new HashMap<>();
    private HashMap<Animal, Vector2d> previousAnimalsPositions;

    EvolutionSimulatorMap map;

    //map1
    @FXML
    private SplitPane mapDisplayer;
    @FXML
    public AnchorPane anchorPaneMap;
    @FXML
    private Label numberOfAnimals;
    @FXML
    private Label averageEnergyOfLivingAnimals;
    @FXML
    private Label numberOfPlants;
    @FXML
    private Label averageLifetimeOfDeadAnimals;
    @FXML
    private Label averageNumberChildOfLivingAnimals;
    @FXML
    private Label dominantGenotype;


    //disactivated map
    @FXML
    private Label chosenAnimal;
    @FXML
    private Label numberOfChildLabel;
    @FXML
    private Label numberOfChild;
    @FXML
    private Label numberOfDescendantsLabel;
    @FXML
    private Label numberOfDescendants;
    @FXML
    private Label dayOfDieLabel;
    @FXML
    private Label dayOfDie;

    MapAndStatisticController(EvolutionSimulatorMap map){
        this.map = map;
        previousAnimalsPositions = new HashMap<>();
        alreadyExecuted = false;
        rows = map.getHeight();
        columns = map.getWidth();
        gridWidth = width / rows;
        gridHeight = height / columns;
    }


//    public void initialize(){
//
//    }

    public void animation(){
        if (!alreadyExecuted) {
            drawSteppeAndJungle();
            drawAnimalsAndPlants();
            alreadyExecuted = true;
        }
        if(!map.isOneOrLessAnimal()){
            removePreviousPlants();
            getPreviousAnimalsPositions();
            map.oneDay();
            animateAnimals();
            drawAnimalsAndPlants();
            removePreviousAnimals();
        }
    }

    public void setStatistics(){
        StatisticOfDay statisticOfDay = map.statistics.getStatisticOfDay(EvolutionSimulatorController.whichDay-1);
        numberOfAnimals.setText(statisticOfDay.getNumberOfAnimal()+"");
        numberOfPlants.setText(statisticOfDay.getNumberOfPlants()+"");
        averageEnergyOfLivingAnimals.setText(String.format("%.2f", statisticOfDay.getAverageEnergyOfLivingAnimals()));
        averageLifetimeOfDeadAnimals.setText(String.format("%.2f", statisticOfDay.getAverageLifetimeOfDeadAnimals()));
        averageNumberChildOfLivingAnimals.setText(String.format("%.2f", statisticOfDay.getAverageNumberChildOfLivingAnimals()));
        dominantGenotype.setText(statisticOfDay.getDominantGenotype().toString());
    }

    public void drawSteppeAndJungle(){
        double gridWidth = width / rows;
        double gridHeight = height / columns;
        // initialize steppe
        for( int i=0; i < rows; i++) {
            for( int j=0; j < columns; j++) {
                Color color = Color.web("rgb(148, 171, 48)");
//                        new Color(148, 171, 48, 1);
                FieldNode node = new FieldNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight, color);
                // add node to group
                anchorPaneMap.getChildren().add(node);

                myNodesMap.put(new Vector2d(i,j), new LinkedList<Node>(Arrays.asList(node)));
            }
        }
        //drawing Jungle
        for( int i= map.getJungleLowerLeft().getX(); i < map.getJungleLowerLeft().getX() + map.getJungleWidth(); i++) {
            for( int j=map.getJungleLowerLeft().getY(); j < map.getJungleLowerLeft().getY() + map.getJungleHeight(); j++) {
                Color color = Color.web("rgb(16, 70, 19)");
//                        new Color(148, 171, 48, 1);
                FieldNode node = new FieldNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight, color);
                Vector2d position = new Vector2d(i,j);
                myNodesMap.get(position).add(node);
                // add node to group
                anchorPaneMap.getChildren().add(node);
            }
        }
    }

    public void getPreviousAnimalsPositions(){
        HashMap<Animal, Vector2d> result = new HashMap<>();
        for(Animal animal:map.getAnimalList()){
            result.put(animal, animal.getPosition());
        }
        previousAnimalsPositions = result;
    }

    public void animateAnimals(){
        Iterator it = previousAnimalsPositions.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();
            Animal animal = (Animal) pair.getKey();
            Vector2d newPosition = animal.getPosition();
            Vector2d oldPosition = (Vector2d) pair.getValue();
            AnimalNode animalNode = (AnimalNode) myNodesMap.get(oldPosition).getLast();
            animalNode.toFront();
            animalNode.changeColorOfAnimal(map.colorForAnimal(animal));

            Path path = new Path();
            path.getElements().add (new MoveTo( animalNode.getTranslateX() + animalNode.getBoundsInParent().getWidth() / 2.0, animalNode.getTranslateY() + animalNode.getBoundsInParent().getHeight() / 2.0));
            path.getElements().add (new LineTo( newPosition.getX() * gridWidth + gridWidth / 2, newPosition.getY() * gridHeight + gridHeight / 2));

            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(MenuController.delay));
            pathTransition.setNode(animalNode);
            pathTransition.setPath(path);
            myNodesMap.get(oldPosition).remove(animalNode);
            myNodesMap.get(newPosition).addLast(animalNode);

            pathTransition.play();
        }
    }

    public void drawAnimalsAndPlants(){
        ArrayList<Plant> plants1 = new ArrayList<>(map.getPlants().values());
        for(Plant plant:plants1) {
            int i = plant.getPosition().getX();
            int j = plant.getPosition().getY();
            PlantNode plantNode = new PlantNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight);
            Vector2d position = new Vector2d(i,j);
            myNodesMap.get(position).add(plantNode);
            anchorPaneMap.getChildren().add(plantNode);
            plantNode.toFront();
        }
        if(previousAnimalsPositions.size() < map.getAnimalList().size()) {
            LinkedList<Animal> newAnimals = new LinkedList<>(map.getAnimalList());
            for(Animal existedAnimal: previousAnimalsPositions.keySet()){
                newAnimals.remove(existedAnimal);
            }
            for (Animal newAnimal : newAnimals) {
                Color color = map.colorForAnimal(newAnimal);
                int i = newAnimal.getPosition().getX();
                int j = newAnimal.getPosition().getY();
                AnimalNode node = new AnimalNode(i * gridWidth, j * gridHeight, gridWidth / 3, gridHeight / 3, color);
                Vector2d position = new Vector2d(i, j);
                myNodesMap.get(position).add(node);
                anchorPaneMap.getChildren().add(node);
            }
        }
    }
    public void removePreviousAnimals(){
        if (previousAnimalsPositions.size() > map.getAnimalList().size()) {
            LinkedList<Animal> deadAnimals = new LinkedList<>(previousAnimalsPositions.keySet());
            //remove living animals from list of death ones
            for(Animal livingAnimal:map.getAnimalList()){
                deadAnimals.remove(livingAnimal);
            }
            for(Animal deadAnimal:deadAnimals){
                Vector2d position = deadAnimal.getPosition();
                AnimalNode animalNode;
                LinkedList<Node> nodes = new LinkedList<>(myNodesMap.get(position));
                for(Node node:nodes){
                    if(node instanceof AnimalNode){
                        animalNode = (AnimalNode) node;
                        myNodesMap.get(position).remove(animalNode);
                        anchorPaneMap.getChildren().remove(animalNode);
                    }
                }
            }
        }
    }
    public void removePreviousPlants(){
        LinkedList<Plant> plants1 = new LinkedList<>(map.getPlants().values());
        for(Plant plant:plants1){
//            int i = plant.getPosition().getX();
//            int j = plant.getPosition().getY();
            Vector2d position = plant.getPosition();
            PlantNode plantNode;
            LinkedList<Node> nodes = new LinkedList<>(myNodesMap.get(position));
            for(Node node:nodes){
                if(node instanceof PlantNode){
                    plantNode = (PlantNode) node;
                    myNodesMap.get(position).remove(plantNode);
                    anchorPaneMap.getChildren().remove(plantNode);
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

        public void changeColorOfAnimal(Color color){
            Circle circle = (Circle) getChildren().get(0);
            circle.setFill(color);
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
