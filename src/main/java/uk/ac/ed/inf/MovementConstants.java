package uk.ac.ed.inf;

/**
 * Constant values related to the drone's movement.
 */

public final class MovementConstants {
    /**
     * Distance tolerance to consider if two LngLat points are "close" to one another.
     */
    public final static double DISTANCE_TOLERANCE = 0.00015;

    /**
     * Length of a single move of the drone in one direction (in degrees).
     */
    public final static double MOVE_LENGTH = 0.00015;

    /**
     * The number of moves the drone can make before it runs out of battery.
     */
    public final static int MAXIMUM_MOVES = 2000;

    /**
     * The coordinates (LngLat point) of Appleton Tower.
     */
    public final static LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);

}
