package uk.ac.ed.inf.movement.model;

import java.util.List;

/**
 * Abstract representation of any area as a polygon.
 */

public abstract class Area {
    private final LngLat[] vertexCoordinates;

    /**
     * Creates an instance of a class which extends Area.
     * @param vertexCoordinates an array of vertices of the polygon, all of them used
     */
    public Area(LngLat[] vertexCoordinates) {
        this.vertexCoordinates = vertexCoordinates;
    }

    /**
     * Creates an instance of a class which extends Area.
     * @param vertexCoordinates an array of vertices of the polygon, the last one is not used
     */
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
