package uk.ac.ed.inf.movement.model;

public record DroneMove(String orderNo, double fromLongitude, double fromLatitude,
                        double angle, double toLongitude, double toLatitude, int ticksSinceStartOfCalculation) {
}
