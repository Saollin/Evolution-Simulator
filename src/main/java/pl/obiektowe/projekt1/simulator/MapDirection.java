package pl.obiektowe.projekt1.simulator;

import java.util.HashMap;
import java.util.Map;

public enum MapDirection {
    EAST(0), NORTH(1), NORTH_EAST(2), NORTH_WEST(3), SOUTH(4), SOUTH_EAST(5), SOUTH_WEST(6), WEST(7);

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
}
