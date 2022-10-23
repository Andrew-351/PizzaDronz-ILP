package uk.ac.ed.inf.model;

/**
 * Representation of the central area.
 *
 * @param vertexCoordinates The array of vertices of the Central area polygon.
 */

public record CentralArea(LngLat[] vertexCoordinates) {
    /**
     * Static method to get the central area from server.
     */
    public static CentralArea getCentralAreaFromRestServer() {
        return new CentralArea((LngLat[]) RestServerClient.getDataFromServer(
                RestServerClient.BASE_URL + RestServerClient.CENTRAL_AREA_ENDPOINT, LngLat[].class));
    }

    /**
     * Returns an array of vertices of the Central area polygon.
     *
     * @return an array of vertices (LngLat points), that form the Central area polygon, in the appropriate order.
     */
    @Override
    public LngLat[] vertexCoordinates() {
        return vertexCoordinates;
    }
}
