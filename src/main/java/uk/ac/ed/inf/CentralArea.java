package uk.ac.ed.inf;

import java.io.IOException;
import java.net.URL;

/**
 * A Singleton representing the Central Area definition.
 */

public final class CentralArea {
    /**
     * The only instance of CentralArea class.
     */
    public static CentralArea instance;

    /**
     * The array of vertices of the Central area polygon.
     */
    private final LngLat[] vertexCoordinates;

    /**
     * Creates an instance of the CentralArea singleton.
     * @throws IOException if the URL is incorrect.
     */
    private CentralArea() throws IOException {
        vertexCoordinates = (LngLat[]) RestServerClient.getDataFromServer(
                new URL(RestServerClient.BASE_URL + RestServerClient.CENTRAL_AREA_ENDPOINT), LngLat[].class);
    }

    /*
      Static block to initialise an instance of the CentralArea singleton.
     */
    static {
        try {
            instance = new CentralArea();
        } catch (IOException ignored) {}
    }

    /**
     * Returns an array of vertices of the Central area polygon.
     * @return an array of vertices (LngLat points), that form the Central area polygon, in the appropriate order.
     */
    public LngLat[] getVertexCoordinates() {
        return vertexCoordinates;
    }
}
