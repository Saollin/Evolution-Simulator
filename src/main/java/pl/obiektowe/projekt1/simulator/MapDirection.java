package pl.obiektowe.projekt1.simulator;

import java.util.HashMap;
import java.util.Map;

public enum MapDirection {
    NORTH(0), NORTH_EAST(1), EAST(2), SOUTH_EAST(3), SOUTH(4), SOUTH_WEST(5), WEST(6), NORTH_WEST(7);

    private final int directionNumber;
    private static final Map<Integer, MapDirection> DIRECTION_MAP = new HashMap<>();

    MapDirection(int directionNumber) {
        this.directionNumber = directionNumber;
    }

    static {
        for (MapDirection md : MapDirection.values()) {
            DIRECTION_MAP.put(md.directionNumber, md);
        }
    }

    public static MapDirection valueOfDirectionNumber(int directionNumber){
        return DIRECTION_MAP.get(directionNumber);
    }

    Vector2d toUnitVector(){
        switch(this){
            case NORTH: return new Vector2d(0,1);
            case NORTH_EAST: return new Vector2d(1,1);
            case EAST: return new Vector2d(1,0);
            case SOUTH_EAST: return new Vector2d(1,-1);
            case SOUTH: return new Vector2d(0,-1);
            case SOUTH_WEST: return new Vector2d(-1,-1);
            case WEST: return new Vector2d(-1,0);
            case NORTH_WEST: return new Vector2d(-1,1);
            default:
                return null;
        }
    }
}
