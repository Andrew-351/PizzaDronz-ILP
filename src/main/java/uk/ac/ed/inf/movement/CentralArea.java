package uk.ac.ed.inf.movement;

import uk.ac.ed.inf.RestServerClient;

/**
 * Representation of the central area.
 *
 */

public class CentralArea extends Area {
    public CentralArea(LngLat[] vertexCoordinates) {
        super(vertexCoordinates);
    }

    /**
     * Static method to get the central area from server.
     */
    public static CentralArea getCentralAreaFromRestServer() {
        return new CentralArea((LngLat[]) RestServerClient.getDataFromServer(
                RestServerClient.BASE_URL + RestServerClient.CENTRAL_AREA_ENDPOINT, LngLat[].class));
    }
}
