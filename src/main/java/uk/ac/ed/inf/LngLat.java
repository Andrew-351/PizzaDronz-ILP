package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LngLat(double lng, double lat) {

    public LngLat(@JsonProperty("longitude") double lng, @JsonProperty("latitude") double lat) {
            this.lng = lng;
            this.lat = lat;
    }

    private double distanceTo (LngLat lngLat) {
        return Math.sqrt(Math.pow(lngLat.lng - lng, 2) + Math.pow(lngLat.lat - lat, 2));
    }

    public boolean inCentralArea() {
        boolean oddEdgesCrossedOnTheLeft = false;
        LngLat[] coordinates = CentralArea.instance.getVertexCoordinates();
        for (int i = 0; i < coordinates.length; i++) {
            double x1 = coordinates[i].lng;
            double y1 = coordinates[i].lat;
            LngLat lngLat1 = new LngLat(x1, y1);

            double x2 = coordinates[(i + 1) % coordinates.length].lng;
            double y2 = coordinates[(i + 1) % coordinates.length].lat;
            LngLat lngLat2 = new LngLat(x2, y2);

            if (lngLat1.distanceTo(this) + lngLat2.distanceTo(this) == lngLat1.distanceTo(lngLat2)) {
                return true;
            }

            if ((y1 < lat && lat <= y2) || (y2 < lat && lat <= y1)) {
                if (x1 + (lat - y1) / (y2 - y1) * (x2 - x1) < lng) {
                    oddEdgesCrossedOnTheLeft = !oddEdgesCrossedOnTheLeft;
                }
            }
        }

        return oddEdgesCrossedOnTheLeft;
    }

    public boolean closeTo(LngLat lngLat) {
        return distanceTo(lngLat) < MovementConstants.DISTANCE_TOLERANCE;
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
