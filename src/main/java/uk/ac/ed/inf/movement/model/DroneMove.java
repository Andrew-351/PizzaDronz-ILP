package uk.ac.ed.inf.movement.model;

/**
 * Representation of the drone's move in the format required for the output file.
 * @param orderNo a string representing the number of the order currently being collected/delivered
 * @param fromLongitude the longitude of the drone at the start of this move
 * @param fromLatitude the latitude of the drone at the start of this move
 * @param angle the angle of travel of the drone in this move
 * @param toLongitude the longitude of the drone at the end of this move
 * @param toLatitude the latitude of the drone at the end of this move
 * @param ticksSinceStartOfCalculation the elapsed ticks (ns) since the computation started
 */

public record DroneMove(String orderNo,
                        double fromLongitude,
                        double fromLatitude,
                        Double angle,
                        double toLongitude,
                        double toLatitude,
                        long ticksSinceStartOfCalculation) {
}
