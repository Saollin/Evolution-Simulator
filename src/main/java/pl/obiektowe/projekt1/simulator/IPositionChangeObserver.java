package pl.obiektowe.projekt1.simulator;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, Object a);
}