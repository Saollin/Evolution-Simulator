package pl.obiektowe.projekt1.simulator.Classes;

import pl.obiektowe.projekt1.simulator.Interfaces.IMapElement;

public class Plant implements IMapElement {

    private Vector2d position;

    public Plant(Vector2d position){
        this.position = position;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }
}