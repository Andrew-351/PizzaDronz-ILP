package uk.ac.ed.inf.model;

import java.util.ArrayList;

public class Flightpath {
    private int lengthInMoves;
    private final LngLat startPoint;
    private final LngLat finishPoint;
    private NoFlyZone[] noFlyZones;
    private final ArrayList<LngLat> allPoints = new ArrayList<>();
    private final ArrayList<LngLat> flightpathPoints = new ArrayList<>();

    /**
     * Matrix where 0 means vertices not connected (or the same point), otherwise distance between them.
     */
    private double[][] visibilityGraph;

    public Flightpath(LngLat startPoint, LngLat finishPoint, NoFlyZone[] noFlyZones) {
        this.startPoint = startPoint;
        this.finishPoint = finishPoint;
        this.noFlyZones = noFlyZones;
        flightpathPoints.add(startPoint);
        flightpathPoints.add(finishPoint);
    }


    private boolean isVisible(LngLat point1, LngLat point2) {
        // get all edges of all no-fly zones
        // check if the line point1 --- point2 crosses any of them

        ArrayList<LngLat[]> allEdges = new ArrayList<>();
        for (var noFlyZone : noFlyZones) {
            LngLat[] vertexCoordinates = noFlyZone.getVertexCoordinates();
            int n = vertexCoordinates.length;
            for (int i = 0; i < n; i++) {
                LngLat[] edge = new LngLat[2];
                edge[0] = vertexCoordinates[i];
                edge[1] = vertexCoordinates[(i+1) % n];
                allEdges.add(edge);
            }
        }

        for (var edge : allEdges) {

        }

        return true;
    }

    private void constructVisibilityGraph() {
        if (isVisible(startPoint, finishPoint)) {
            return;
        }

        allPoints.add(startPoint);
        allPoints.add(finishPoint);
        int n = 2;      // visibility graph contains start and finish points
        int[] noFlyZoneStartIndices = new int[noFlyZones.length+1];
        for (int i = 0; i < noFlyZones.length; i++) {
            noFlyZoneStartIndices[i+1] = n;
            for (var vertex : noFlyZones[i].getVertexCoordinates()) {
                allPoints.add(vertex);
                n++;
            }
        }
        noFlyZoneStartIndices[noFlyZones.length+1] = n;

        visibilityGraph = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                visibilityGraph[i][j] = 0;
            }
        }

        int currentIndex = 0;
        for (int i = 0; i < n - 1; i++) {
            int lastPoint = -1;
            if (i == noFlyZoneStartIndices[currentIndex]) {
                lastPoint = noFlyZoneStartIndices[currentIndex+1] - 1;
                currentIndex++;
            }
            for (int j = i + 1; j < n; j++) {
                boolean edgeExists = false;
                if (i > 1) {
                    // adjacent vertices of n/f zone
                    if ((j == i + 1 && j != noFlyZoneStartIndices[currentIndex])) {
                        edgeExists = true;
                    }
                    // first and last vertices of n/f zone
                    else if (lastPoint != -1 && j == lastPoint) {
                        edgeExists = true;
                        lastPoint = -1;
                    }
                    // if same n/f zones but not adjacent
                    else if (j < noFlyZoneStartIndices[currentIndex]) {
                        continue;
                    }
                }
                if (!edgeExists && isVisible(allPoints.get(i), allPoints.get(j))) {
                    edgeExists = true;
                }

                if (edgeExists) {
                    double distance = allPoints.get(i).distanceTo(allPoints.get(j));
                    visibilityGraph[i][j] = distance;
                    visibilityGraph[j][i] = distance;
                }
            }
        }
    }

    private void findShortestPath() {
        if (visibilityGraph != null) {
            // find the shortest path in the graph
        }
    }
}
