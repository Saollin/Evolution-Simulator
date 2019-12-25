package pl.obiektowe.projekt1.simulator.Visualization;

import java.util.Random;

import javafx.animation.Animation.Status;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class NodeDemo extends Application {

    private double sceneWidth = 1024;
    private double sceneHeight = 768;

    private int n = 10;
    private int m = 10;

    double gridWidth = sceneWidth / n;
    double gridHeight = sceneHeight / m;

    MyNode[][] playfield = new MyNode[n][m];

    @Override
    public void start(Stage primaryStage) {


        Group root = new Group();

        // initialize playfield
        for( int i=0; i < n; i++) {
            for( int j=0; j < m; j++) {

                // create node
                MyNode node = new MyNode( "Item " + i + "/" + j, i * gridWidth, j * gridHeight, gridWidth, gridHeight);

                // add node to group
                root.getChildren().add( node);

                // add to playfield for further reference using an array
                playfield[i][j] = node;

            }
        }


        Scene scene = new Scene( root, sceneWidth, sceneHeight);

        primaryStage.setScene( scene);
        primaryStage.show();


        animate();

    }

    private void animate() {

        Random random = new Random();

        int ai = random.nextInt(n);
        int aj = random.nextInt(m);

        int bi = random.nextInt(n);
        int bj = random.nextInt(m);

        // make sure that A and B are never the same
        if( ai == bi && aj == bj) {
            ai++;
            if( ai >= n)
                ai = 0;
        }

        MyNode nodeA = playfield[ai][aj];
        nodeA.toFront();

        MyNode nodeB = playfield[bi][bj];
        nodeB.toFront();

        // swap on array to keep array consistent
        playfield[ai][aj] = nodeB;
        playfield[bi][bj] = nodeA;

        // A -> B
        Path pathA = new Path();
        pathA.getElements().add (new MoveTo ( nodeA.getTranslateX() + nodeA.getBoundsInParent().getWidth() / 2.0, nodeA.getTranslateY() + nodeA.getBoundsInParent().getHeight() / 2.0));
        pathA.getElements().add (new LineTo( nodeB.getTranslateX() + nodeB.getBoundsInParent().getWidth() / 2.0, nodeB.getTranslateY() + nodeB.getBoundsInParent().getHeight() / 2.0));

        PathTransition pathTransitionA = new PathTransition();
        pathTransitionA.setDuration(Duration.millis(1000));
        pathTransitionA.setNode( nodeA);
        pathTransitionA.setPath(pathA);

        pathTransitionA.play();

        // B -> A
        Path pathB = new Path();
        pathB.getElements().add (new MoveTo ( nodeB.getTranslateX() + nodeB.getBoundsInParent().getWidth() / 2.0, nodeB.getTranslateY() + nodeB.getBoundsInParent().getHeight() / 2.0));
        pathB.getElements().add (new LineTo( nodeA.getTranslateX() + nodeA.getBoundsInParent().getWidth() / 2.0, nodeA.getTranslateY() + nodeA.getBoundsInParent().getHeight() / 2.0));

        PathTransition pathTransitionB = new PathTransition();
        pathTransitionB.setDuration(Duration.millis(1000));
        pathTransitionB.setNode( nodeB);
        pathTransitionB.setPath(pathB);

        pathTransitionB.play();

        pathTransitionA.setOnFinished( new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if( pathTransitionB.getStatus() == Status.RUNNING)
                    return;

                animate();
            }
        });

        pathTransitionB.setOnFinished( new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if( pathTransitionA.getStatus() == Status.RUNNING)
                    return;

                animate();
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class MyNode extends StackPane {

        public MyNode( String name, double x, double y, double width, double height) {

            // create rectangle
            Rectangle rectangle = new Rectangle( width, height);
            rectangle.setStroke(Color.BLACK);
            rectangle.setFill(Color.LIGHTBLUE);

            // create label
            Label label = new Label( name);

            // set position
            setTranslateX( x);
            setTranslateY( y);

            getChildren().addAll( rectangle, label);

        }

    }
}
