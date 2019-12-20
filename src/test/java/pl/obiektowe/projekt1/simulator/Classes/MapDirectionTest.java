package pl.obiektowe.projekt1.simulator.Classes;

import org.junit.jupiter.api.Test;
import pl.obiektowe.projekt1.simulator.Classes.Vector2d;
import pl.obiektowe.projekt1.simulator.Enum.MapDirection;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {

    @Test
    void classShouldReturnRightUnitVectors(){
        Vector2d north = new Vector2d(0,1);
        Vector2d northEast = new Vector2d(1,1);
        Vector2d east = new Vector2d(1,0);
        Vector2d southEast = new Vector2d(1,-1);
        Vector2d south = new Vector2d(0,-1);
        Vector2d southWest = new Vector2d(-1,-1);
        Vector2d west = new Vector2d(-1,0);
        Vector2d northWest = new Vector2d(-1, 1);
        assertAll(()->{
            assertEquals(north, MapDirection.NORTH.toUnitVector());
            assertEquals(northEast, MapDirection.NORTH_EAST.toUnitVector());
            assertEquals(east, MapDirection.EAST.toUnitVector());
            assertEquals(southEast, MapDirection.SOUTH_EAST.toUnitVector());
            assertEquals(south, MapDirection.SOUTH.toUnitVector());
            assertEquals(southWest, MapDirection.SOUTH_WEST.toUnitVector());
            assertEquals(west, MapDirection.WEST.toUnitVector());
            assertEquals(northWest, MapDirection.NORTH_WEST.toUnitVector());
        });
    }

    @Test
    void classShouldReturnRightMapDirectionToTheRightNumber(){
        assertAll(()->{
            assertEquals(MapDirection.NORTH, MapDirection.valueOfDirectionNumber(0));
            assertEquals(MapDirection.NORTH_EAST, MapDirection.valueOfDirectionNumber(1));
            assertEquals(MapDirection.EAST, MapDirection.valueOfDirectionNumber(2));
            assertEquals(MapDirection.SOUTH_EAST, MapDirection.valueOfDirectionNumber(3));
            assertEquals(MapDirection.SOUTH, MapDirection.valueOfDirectionNumber(4));
            assertEquals(MapDirection.SOUTH_WEST, MapDirection.valueOfDirectionNumber(5));
            assertEquals(MapDirection.WEST, MapDirection.valueOfDirectionNumber(6));
            assertEquals(MapDirection.NORTH_WEST, MapDirection.valueOfDirectionNumber(7));
        });
    }
}