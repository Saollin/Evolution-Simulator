package pl.obiektowe.projekt1.simulator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvolutionSimulatorMapTest {

    private EvolutionSimulatorMap map = new EvolutionSimulatorMap(100,100,0.25,20,1, 4);

    @Test
    void positionSmallerXThanLowerLeftShouldBeAtTheSameDistanceFromRightBound() {
        //given
        Vector2d wrongPosition = new Vector2d(-1,10);
        Vector2d expected = new Vector2d(99,10);

        //when
        Vector2d countedPosition = map.countRightPositionOnTheMap(wrongPosition);

        //then
        assertEquals(expected, countedPosition);
    }

    @Test
    void positionSmallerYThanLowerLeftShouldBeAtTheSameDistanceFromUpperBound() {
        //given
        Vector2d wrongPosition = new Vector2d(10,-2);
        Vector2d expected = new Vector2d(10,98);

        //when
        Vector2d countedPosition = map.countRightPositionOnTheMap(wrongPosition);

        //then
        assertEquals(expected, countedPosition);
    }

    @Test
    void positionSmallerForBothXAndYThanLowerLeftShouldBeAtTheSameDistanceFromUpperRight() {
        //given
        Vector2d wrongPosition = new Vector2d(-1,-2);
        Vector2d expected = new Vector2d(99,98);

        //when
        Vector2d countedPosition = map.countRightPositionOnTheMap(wrongPosition);

        //then
        assertEquals(expected, countedPosition);
    }

    @Test
    void positionShouldBeTheSameForPositionInsideTheMap(){
        //given
        Vector2d expected = new Vector2d(10, 10);

        //when
        Vector2d countedPosition = map.countRightPositionOnTheMap(expected);

        //then
        assertEquals(expected,countedPosition);
    }
}