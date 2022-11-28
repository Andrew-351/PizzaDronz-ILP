package uk.ac.ed.inf.movement;

import java.util.List;

public class Area {
    /**
     * The array of vertices of the area polygon.
     */
    private final LngLat[] vertexCoordinates;

    public Area(LngLat[] vertexCoordinates) {
        this.vertexCoordinates = vertexCoordinates;
    }

    public Area(List<List<Double>> vertexCoordinates) {
        this.vertexCoordinates = new LngLat[vertexCoordinates.size() - 1];
        for (int i = 0; i < vertexCoordinates.size() - 1; i++) {
            this.vertexCoordinates[i] = new LngLat(vertexCoordinates.get(i).get(0), vertexCoordinates.get(i).get(1));
        }
    }

    /**
     * Returns an array of vertices of the area polygon.
     * @return an array of vertices (LngLat points), that form the area polygon, in the appropriate order.
     */
    public LngLat[] getVertexCoordinates() {
        return vertexCoordinates;
    }
}
