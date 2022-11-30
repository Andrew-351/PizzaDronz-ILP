package uk.ac.ed.inf.movement.model;

public record DroneMove(String orderNo, double fromLongitude, double fromLatitude,
                        Double angle, double toLongitude, double toLatitude, long ticksSinceStartOfCalculation) {
}
