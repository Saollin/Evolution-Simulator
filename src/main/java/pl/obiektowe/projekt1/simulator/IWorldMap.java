package pl.obiektowe.projekt1.simulator;

public interface IWorldMap {


    /**
     * Place a animal on the map.
     *
     * @param element The element to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the map is already occupied.
     */
    boolean place(IMapElement element);

    Vector2d countRightPositionOnTheMap(Vector2d position);

    /**
     * Return an object at a given position.
     *
     * @param position The position of the object.
     * @return Object or null if the position is not occupied.
     */
    Object objectAt(Vector2d position);
}
