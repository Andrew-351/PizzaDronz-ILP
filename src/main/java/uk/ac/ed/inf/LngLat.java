package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LngLat(double lng, double lat) {

    public LngLat(@JsonProperty("longitude") double lng, @JsonProperty("latitude") double lat) {
        /*
          Longitude has to be in the range from -180 to 180 degrees.
          Latitude has to be in the range from -90 to 90 degrees.
         */
        if (lng >= -180 && lng <= 180 && lat >= -90 && lat <= 90) {
            this.lng = lng;
            this.lat = lat;
        }
        else {
            throw new IllegalArgumentException("Invalid longitude/latitude values.");
        }
    }

    public boolean inCentralArea() {
        boolean oddEdgesCrossedOnTheLeft = false;
        LngLat[] coordinates = CentralArea.instance.vertexCoordinates;
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
}
