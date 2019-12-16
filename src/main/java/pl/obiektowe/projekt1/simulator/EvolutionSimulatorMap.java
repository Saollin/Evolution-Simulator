package pl.obiektowe.projekt1.simulator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class EvolutionSimulatorMap implements IPositionChangeObserver, IWorldMap {

    //vectors of Map
    private final Vector2d mapUpperRight;
    private final Vector2d mapLowerLeft;
    private final Vector2d jungleUpperRight;
    private final Vector2d jungleLowerLeft;
    private final int width;
    private final int height;

    //vectors of Jungle
    private final int jungleWidth;
    private final int jungleHeight;
    private final double jungleRatio;

    //Energy
    private int startEnergy;
    private int moveEnergy;
    private int plantEnergy;

    //data of object
    private Map<Vector2d,Plant> plants = new HashMap<>();
    private Map<Vector2d, LinkedList<Animal>> animals = new HashMap<>();
    private LinkedList<Animal> animalList = new LinkedList<>();
    private LinkedList<Plant> plantList = new LinkedList<>();

    public EvolutionSimulatorMap(int width, int height, double jungleRatio, int startEnergy, int moveEnergy, int plantEnergy) {
        this.width = width;
        this.height = height;
        this.mapLowerLeft = new Vector2d(0,0);
        this.mapUpperRight = new Vector2d(width-1,height-1);

        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.startEnergy = (-1) * startEnergy;

        this.jungleRatio = jungleRatio;
        this.jungleWidth = (int) jungleRatio * width;
        this.jungleHeight = (int) jungleRatio * height;

        //coordinates of jungle
        int xLowerLeft = (int)(width * 0.5 - 0.5 * jungleWidth);
        int yLowerLeft = (int)(height * 0.5 - 0.5 * jungleHeight);
        int xUpperRight = (int)(width * 0.5 + 0.5 * jungleWidth);
        int yUpperRight = (int)(height * 0.5 + 0.5 * jungleHeight);

        this.jungleLowerLeft = new Vector2d(xLowerLeft, yLowerLeft);
        this.jungleUpperRight = new Vector2d(xUpperRight, yUpperRight);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {

    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return false;
    }

    @Override
    public boolean place(IMapElement element) {
        return false;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return false;
    }

    @Override
    public Object objectAt(Vector2d position) {
        return null;
    }
}
