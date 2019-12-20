package pl.obiektowe.projekt1.simulator.Interfaces;

import pl.obiektowe.projekt1.simulator.Classes.Vector2d;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, Object a);
}