package uk.ac.ed.inf;

import java.io.IOException;
import java.net.URL;

public final class CentralArea {
    public static CentralArea instance;
    private final LngLat[] vertexCoordinates;

    public CentralArea() throws IOException {
        vertexCoordinates = (LngLat[]) RestServerClient.getDataFromServer(
                new URL(RestServerClient.BASE_URL + RestServerClient.CENTRAL_AREA_ENDPOINT), LngLat[].class);
    }

    static {
        try {
            instance = new CentralArea();
        } catch (IOException ignored) {}
    }

    public LngLat[] getVertexCoordinates() {
        return vertexCoordinates;
    }
}
