package uk.ac.ed.inf.movement;

public record DroneMove(String orderNo, double fromLongitude, double fromLatitude,
                        Double angle, double toLongitude, double toLatitude, long ticksSinceStartOfCalculation) {
}
