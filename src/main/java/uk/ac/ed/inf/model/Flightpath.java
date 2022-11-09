package uk.ac.ed.inf.model;

import java.util.ArrayList;

public class Flightpath {
    private int lengthInMoves;
    private final LngLat startPoint;
    private final LngLat finishPoint;
    private final NoFlyZone[] noFlyZones;
    private final ArrayList<LngLat> allPoints = new ArrayList<>();
    private final ArrayList<LngLat[]> allEdges = new ArrayList<>();
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

    // Check if p2 is between p1 and p3, assuming they are all collinear
    private boolean collinearP2BetweenP1AndP3(LngLat p1, LngLat p2, LngLat p3) {
        return p2.lng() <= Math.max(p1.lng(), p3.lng()) && p2.lng() >= Math.min(p1.lng(), p3.lng()) &&
               p2.lat() <= Math.max(p1.lat(), p3.lat()) && p2.lat() >= Math.min(p1.lat(), p3.lat());
    }

    // Find the orientation of three points:
    // 0 => collinear
    // 1 => Clockwise
    // -1 => Counterclockwise
    private int threePointOrientation(LngLat p1, LngLat p2, LngLat p3) {
        double orientation = (p2.lat() - p1.lat()) * (p3.lng() - p2.lng()) -
                             (p2.lng() - p1.lng()) * (p3.lat() - p2.lat());
        if (orientation == 0) {
            return 0;
        }
        return (orientation > 0) ? 1 : -1;
    }

    // Check if the line p1 --- p2 crosses any of all the edges
    private boolean isVisible(LngLat p1, LngLat p2) {
        for (var edge : allEdges) {
            if (p1.equals(edge[0]) || p1.equals(edge[1]) || p2.equals(edge[0]) || p2.equals(edge[1])) {
                continue;
            }

            int orientation1 = threePointOrientation(p1, p2, edge[0]);
            int orientation2 = threePointOrientation(p1, p2, edge[1]);
            int orientation3 = threePointOrientation(edge[0], edge[1], p1);
            int orientation4 = threePointOrientation(edge[0], edge[1], p2);
            if (orientation1 != orientation2 && orientation3 != orientation4) {
                return false;
            }

            if ((orientation1 == 0 && collinearP2BetweenP1AndP3(p1, edge[0], p2)) ||
                (orientation2 == 0 && collinearP2BetweenP1AndP3(p1, edge[1], p2)) ||
                (orientation3 == 0 && collinearP2BetweenP1AndP3(edge[0], p1, edge[1])) ||
                (orientation4 == 0 && collinearP2BetweenP1AndP3(edge[0], p2, edge[1]))) {
                return false;
            }
        }

        return true;
    }

    public void constructVisibilityGraph() {
        allPoints.add(startPoint);
        allPoints.add(finishPoint);
        int n = 2;      // visibility graph contains start and finish points
        int[] noFlyZoneStartIndices = new int[noFlyZones.length+1];
        for (int i = 0; i < noFlyZones.length; i++) {
            noFlyZoneStartIndices[i] = n;
            LngLat[] vertexCoordinates = noFlyZones[i].getVertexCoordinates();
            int m = vertexCoordinates.length;
            for (int j = 0; j < m; j++) {
                n++;
                LngLat[] edge = new LngLat[2];
                edge[0] = vertexCoordinates[j];
                edge[1] = vertexCoordinates[(j+1) % m];
                allPoints.add(edge[0]);
                allEdges.add(edge);
            }
        }
        noFlyZoneStartIndices[noFlyZones.length] = n;

        if (isVisible(startPoint, finishPoint)) {
            return;
        }

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

    public void findShortestPath() {
        if (visibilityGraph != null) {
            // find the shortest path in the graph
        }
    }

    public int getLengthInMoves() {
        return lengthInMoves;
    }

    public ArrayList<LngLat> getAllPoints() {
        return allPoints;
    }

    public ArrayList<LngLat[]> getAllEdges() {
        return allEdges;
    }

    public ArrayList<LngLat> getFlightpathPoints() {
        return flightpathPoints;
    }

    public double[][] getVisibilityGraph() {
        return visibilityGraph;
    }
}
