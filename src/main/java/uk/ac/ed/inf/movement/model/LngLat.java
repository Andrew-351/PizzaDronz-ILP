package uk.ac.ed.inf.movement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of a point on the map with two coordinates in degrees.
 * @param lng longitude
 * @param lat latitude
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public record LngLat(double lng, double lat) {

    public LngLat(@JsonProperty("longitude") double lng, @JsonProperty("latitude") double lat) {
            this.lng = lng;
            this.lat = lat;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        LngLat lngLat = (LngLat) obj;
        return lng == lngLat.lng && lat == lngLat.lat;
    }

    @Override
    public int hashCode() {
        final int prime = 29;
        int result = 1;
        result = prime * result + ((Double) lng).hashCode();
        result = prime * result + ((Double) lat).hashCode();
        return result;
    }

    /**
     * Calculates the distance from the point to another LngLat point.
     * @param lngLat a LngLat point on the map
     * @return the Pythagorean distance between THIS point and the parameter point.
     */
    public double distanceTo (LngLat lngLat) {
        return Math.sqrt(Math.pow(lngLat.lng - lng, 2) + Math.pow(lngLat.lat - lat, 2));
    }

    /**
     * Determines if the LngLat point is within the Central area.
     * @param centralArea the Central Area
     * @return true if the point is within the Central area; false otherwise.
     */
    public boolean inCentralArea(CentralArea centralArea) {
        if (centralArea == null) {
            return false;
        }

        boolean oddEdgesCrossedOnTheLeft = false;
        LngLat[] coordinates = centralArea.vertexCoordinates();
        for (int i = 0; i < coordinates.length; i++) {
            double x1 = coordinates[i].lng;
            double y1 = coordinates[i].lat;
            LngLat lngLat1 = new LngLat(x1, y1);

            double x2 = coordinates[(i + 1) % coordinates.length].lng;
            double y2 = coordinates[(i + 1) % coordinates.length].lat;
            LngLat lngLat2 = new LngLat(x2, y2);

            /*
              If the point is on the edge of the polygon, it is considered to be inside it.
              The point being on the edge means the distance from one vertex to the other vertex
              equals the sum of the distances from each of the vertices to the point.
             */
            if (lngLat1.distanceTo(this) + lngLat2.distanceTo(this) == lngLat1.distanceTo(lngLat2)) {
                return true;
            }

            /*
              The point is inside the polygon if there is an odd number of edges crossed by
              the line y = this.lat
             */
            if ((y1 < lat && lat <= y2) || (y2 < lat && lat <= y1)) {
                if (x1 + (lat - y1) / (y2 - y1) * (x2 - x1) < lng) {
                    oddEdgesCrossedOnTheLeft = !oddEdgesCrossedOnTheLeft;
                }
            }
        }

        return oddEdgesCrossedOnTheLeft;
    }

    /**
     * Determines if the point is "close" (within the distance tolerance) to another LngLat point.
     * @param lngLat a LngLat point on the map
     * @return true if THIS point is "close" to the parameter point; false otherwise.
     */
    public boolean closeTo(LngLat lngLat) {
        return distanceTo(lngLat) < MovementConstants.DISTANCE_TOLERANCE;
    }

    /**
     * Determines the new position of the drone after it makes a move.
     * @param direction one of the compass directions in which the drone moves or a "hover" move
     * @return the new position as a LngLat point.
     */
    public LngLat nextPosition (CompassDirection direction) {
        if (direction == CompassDirection.HOVER) {
            return this;
        }
        return new LngLat (
            lng + MovementConstants.MOVE_LENGTH * Math.cos(Math.toRadians(direction.getAngle())),
            lat + MovementConstants.MOVE_LENGTH * Math.sin(Math.toRadians(direction.getAngle())));
    }
}
