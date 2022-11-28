package uk.ac.ed.inf.movement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ed.inf.RestServerClient;
import java.util.List;

/**
 * Representation of a no-fly zone.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public final class NoFlyZone extends Area{
    /**
     * Creates an instance of the NoFlyZone class.
     * @param vertexCoordinates coordinates of the vertices of the no-fly zone.
     */
    public NoFlyZone(@JsonProperty("coordinates") List<List<Double>> vertexCoordinates) {
        super(vertexCoordinates);
    }

    /**
     * Static method to get an array of all no-fly zones from server.
     */
    static NoFlyZone[] getNoFlyZonesFromRestServer() {
        return (NoFlyZone[]) RestServerClient.getDataFromServer(
                RestServerClient.BASE_URL + RestServerClient.NO_FLY_ZONES_ENDPOINT, NoFlyZone[].class);
    }
}
