package pl.obiektowe.projekt1.simulator;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class EvolutionSimulatorMapTest {

    private EvolutionSimulatorMap map = new EvolutionSimulatorMap(100,100,0.25,20,1, 4);

    @Test
    void shouldCreatingNewMapLastNoLongerThan5Ms(){
        assertTimeout(Duration.ofMillis(5), () -> {
            EvolutionSimulatorMap newMap = new EvolutionSimulatorMap(100,100,0.5, 20,1,10);
        });
    }

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

    @Test
    void placedAnimalIsInsideLists(){
        //given
        Vector2d position = new Vector2d(2,2);
        Animal animal = new Animal(map, position, 40);

        //when
        map.place(animal);

        //then
        assertAll(()->{
            assertEquals(animal, map.getAnimals().get(position).getFirst());
            assertTrue(map.getAnimalList().contains(animal));
        });
    }

    @Test
    void placedPlantIsInsideLists(){
        //given
        Vector2d position = new Vector2d(2,2);
        Plant plant = new Plant(position);

        //when
        map.place(plant);

        //then
        assertAll(()->{
            assertEquals(plant, map.getPlants().get(position));
        });
    }

    @Test
    void plantIsNotPlacedInThePositionWhichIsOccupied(){
        //given
        Vector2d position = new Vector2d(2,2);
        Plant plant1 = new Plant(position);
        Plant plant2 = new Plant(position);

        //when
        map.place(plant1);

        //then
        assertAll(()->{
            assertFalse(map.place(plant2));
            assertNotEquals(plant2, map.getPlants().get(position));
        });
    }

    @Test
    void shouldSpawnGrassLastNoLongerThan5MsAtFirstSpawning(){
        //given
        //when
        //then
        assertTimeout(Duration.ofMillis(5), () -> {
            map.spawnGrassInSteppeAndJungle();
        });
    }

    /*
    After 500 spawning should be all positions occupied.
     */
    @Test
    void shouldSpawnGrassLastNoLongerThan5MsAfter500Spawning(){
        //given
        //when
        for(int i = 0; i < 500; i++){
            map.spawnGrassInSteppeAndJungle();
        }
        //then
        assertTimeout(Duration.ofMillis(5), () -> {
            map.spawnGrassInSteppeAndJungle();
        });
    }

    @Test
    void shouldCanPlantBePlacedReturnTrueForEmptyPlace(){
        EvolutionSimulatorMap newMap = new EvolutionSimulatorMap(50,50,0.5,30,1,10);
        assertTrue(newMap.canPlantBePlaced(new Vector2d(3,3)));
    }

    @Test
    void shouldCanPlantBePlacedReturnFalseForOccupiedPlace(){
        EvolutionSimulatorMap newMap = new EvolutionSimulatorMap(50,50,0.5,30,1,10);
        Vector2d position = new Vector2d(2,5);
        Animal animal = new Animal(newMap,position,10);
        newMap.place(animal);
        assertFalse(newMap.canPlantBePlaced(position));
    }
}