package uk.ac.ed.inf.movement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ed.inf.RestServerClient;
import java.util.List;

/**
 * Representation of a no-fly zone.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public final class NoFlyZone {
    /**
     * The array of vertices of the no-fly zone polygon.
     */
    private final LngLat[] vertexCoordinates;

    /**
     * Creates an instance of the NoFlyZone class.
     * @param vertexCoordinates coordinates of the vertices of the no-fly zone.
     */
    public NoFlyZone(@JsonProperty("coordinates") List<List<Double>> vertexCoordinates) {
        this.vertexCoordinates = new LngLat[vertexCoordinates.size() - 1];
        for (int i = 0; i < vertexCoordinates.size() - 1; i++) {
            this.vertexCoordinates[i] = new LngLat(vertexCoordinates.get(i).get(0), vertexCoordinates.get(i).get(1));
        }
    }

    /**
     * Static method to get an array of all no-fly zones from server.
     */
    static NoFlyZone[] getNoFlyZonesFromRestServer() {
        return (NoFlyZone[]) RestServerClient.getDataFromServer(
                RestServerClient.BASE_URL + RestServerClient.NO_FLY_ZONES_ENDPOINT, NoFlyZone[].class);
    }

    /**
     * Returns an array of vertices of the No-Fly zone polygon.
     * @return an array of vertices (LngLat points), that form the No-Fly zone polygon, in the appropriate order.
     */
    public LngLat[] getVertexCoordinates() {
        return vertexCoordinates;
    }
}