package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LngLat(double lng, double lat) {

    public LngLat(@JsonProperty("longitude") double lng, @JsonProperty("latitude") double lat) {
            this.lng = lng;
            this.lat = lat;
    }

    public boolean inCentralArea() {
        boolean oddEdgesCrossedOnTheLeft = false;
        LngLat[] coordinates = CentralArea.instance.getVertexCoordinates();
        for (int i = 0; i < coordinates.length; i++) {
            double x1 = coordinates[i].lng;
            double y1 = coordinates[i].lat;
            double x2 = coordinates[(i + 1) % coordinates.length].lng;
            double y2 = coordinates[(i + 1) % coordinates.length].lat;
            if ((y1 < lat && lat <= y2) || (y2 < lat && lat <= y1)) {
                if (x1 + (lat - y1) / (y2 - y1) * (x2 - x1) < lng) {
                    oddEdgesCrossedOnTheLeft = !oddEdgesCrossedOnTheLeft;
                }
            }
        }

        return oddEdgesCrossedOnTheLeft;
    }

    private double distanceTo (LngLat point) {
        return Math.sqrt(Math.pow(point.lng - lng, 2) + Math.pow(point.lat - lat, 2));
    }

    public boolean closeTo(LngLat point) {
        return distanceTo(point) < MovementConstants.DISTANCE_TOLERANCE;
    }

    public LngLat nextPosition (CompassDirection direction) {
        if (direction == CompassDirection.HOVER) {
            return this;
        }
        return new LngLat (
            lng + MovementConstants.MOVE_LENGTH * Math.cos(Math.toRadians(direction.getAngle())),
            lat + MovementConstants.MOVE_LENGTH * Math.sin(Math.toRadians(direction.getAngle())));
    }
}
