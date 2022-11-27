package uk.ac.ed.inf.movement;

import java.util.ArrayList;

/**
 * Representation of a path between two points (e.g. Appleton Tower and a Restaurant).
 */
public final class Flightpath {
    private final LngLat startPoint;
    private final LngLat finishPoint;
    private final NoFlyZone[] noFlyZones;
    private final ArrayList<LngLat> allPoints = new ArrayList<>();
    private final ArrayList<LngLat[]> allEdges = new ArrayList<>();
    private final ArrayList<LngLat> approximatePoints = new ArrayList<>();
    private final ArrayList<LngLat> droneMovesPoints = new ArrayList<>();
    private final ArrayList<CompassDirection> droneMovesDirections = new ArrayList<>();

    /**
     * Matrix where 0 means vertices not connected (or the same point), otherwise distance between them.
     */
    private double[][] visibilityGraph;

    public Flightpath(LngLat startPoint, LngLat finishPoint, NoFlyZone[] noFlyZones) {
        this.startPoint = startPoint;
        this.finishPoint = finishPoint;
        this.noFlyZones = noFlyZones;
        approximatePoints.add(startPoint);
        approximatePoints.add(finishPoint);
    }

    // Check if p2 is between p1 and p3, assuming they are all collinear
    private boolean collinearP2BetweenP1AndP3(LngLat p1, LngLat p2, LngLat p3) {
        return p2.lng() <= Math.max(p1.lng(), p3.lng()) && p2.lng() >= Math.min(p1.lng(), p3.lng()) &&
               p2.lat() <= Math.max(p1.lat(), p3.lat()) && p2.lat() >= Math.min(p1.lat(), p3.lat());
    }

    // Find the orientation of three points:
    // 0 => Collinear
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

    private void constructVisibilityGraph() {
        if (noFlyZones == null) {
            return;
        }

        allPoints.add(startPoint);
        allPoints.add(finishPoint);
        int n = 2;      // visibility graph contains start and finish points
        int[] noFlyZoneStartIndices = new int[noFlyZones.length + 1];
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

    private int findNearestVertex(double[] distance, boolean[] visited) {
        double minDistance = Double.MAX_VALUE;
        int minDistanceVertex = -1;
        for (int i = 0; i < distance.length; i++) {
            if (distance[i] < minDistance && !visited[i]) {
                minDistance = distance[i];
                minDistanceVertex = i;
            }
        }
        return minDistanceVertex;
    }

    void findShortestPath() {
        constructVisibilityGraph();
        // if visibilityGraph == null => there are only two points - start and finish
        if (visibilityGraph == null) {
            return;
        }

        // Use Dijkstra's Algorithm to find the shortest path
        int n = allPoints.size();
        double[] distance = new double[n];
        int[] previous = new int[n];
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            distance[i] = Double.MAX_VALUE;
            previous[i] = -1;
            visited[i] = false;
        }
        distance[0] = 0;

        for (int i = 0; i < n; i++) {
            int v = findNearestVertex(distance, visited);
            if (v == -1) {
                break;
            }
            visited[v] = true;
            for (int j = 0; j < n; j++) {
                if (!visited[j] && visibilityGraph[v][j] != 0 && (distance[v] + visibilityGraph[v][j] < distance[j])) {
                    distance[j] = distance[v] + visibilityGraph[v][j];
                    previous[j] = v;
                }
            }
        }
        int previousVertex = previous[1];
        while (previousVertex != 0) {
            approximatePoints.add(1, allPoints.get(previousVertex));
            previousVertex = previous[previousVertex];
        }

    }

    void optimiseFlightpath() {
        if (visibilityGraph == null) {
            return;
        }

        int nextPoint = 0;
        while (nextPoint < approximatePoints.size() - 2) {
            LngLat currentPoint = approximatePoints.get(nextPoint);
            for (int i = approximatePoints.size() - 1; i > nextPoint; i--) {
                if (isVisible(currentPoint, approximatePoints.get(i))) {
                    int numberOfPointsToRemove = i - nextPoint - 1;
                    while (numberOfPointsToRemove > 0) {
                        approximatePoints.remove(nextPoint + 1);
                        numberOfPointsToRemove--;
                    }
                }
            }
            nextPoint++;
        }
    }

    /**
     * Find the best next point for the drone to go to when connecting two approximate points of the flightpath.
     * If possible, always try to find a point from which current goal would remain visible.
     * If impossible, just return a point which minimises the distance to the current goal.
     *
     * @param currentPoint current position of the drone
     * @param nextApproximatePoint current goal that is trying to be reached (either a no-fly zone vertex or the restaurant)
     * @return the best point for the drone to go to as its next move
     */
    private LngLat findNextPoint(LngLat currentPoint, LngLat nextApproximatePoint) {
        double minDistanceToGoal = Double.MAX_VALUE;
        LngLat bestPoint = null;
        CompassDirection bestDirection = null;

        double minDistanceToGoalWithGoalVisible = currentPoint.distanceTo(nextApproximatePoint);
        LngLat bestPointWithGoalVisible = null;
        CompassDirection bestDirectionWithGoalVisible = null;

        for (var direction : CompassDirection.values()) {
            if (direction == CompassDirection.HOVER) {
                continue;
            }
            LngLat nextPoint = currentPoint.nextPosition(direction);

            // Only consider next points by reaching which the drone doesn't cross a no-fly zone edge.
            if (isVisible(currentPoint, nextPoint)) {
                double distanceToGoal = nextPoint.distanceTo(nextApproximatePoint);

                // Find the best next point from which the current goal would be visible.
                if (isVisible(nextPoint, nextApproximatePoint) &&
                        distanceToGoal < minDistanceToGoalWithGoalVisible) {
                    bestPointWithGoalVisible = nextPoint;
                    bestDirectionWithGoalVisible = direction;
                    minDistanceToGoalWithGoalVisible = distanceToGoal;
                }

                // Find the best next point that just minimises the distance to the current goal.
                if (distanceToGoal < minDistanceToGoal) {
                    bestPoint = nextPoint;
                    bestDirection = direction;
                    minDistanceToGoal = distanceToGoal;
                }
            }
        }
        if (bestPointWithGoalVisible != null) {
            bestPoint = bestPointWithGoalVisible;
            bestDirection = bestDirectionWithGoalVisible;
        }
        droneMovesPoints.add(bestPoint);
        droneMovesDirections.add(bestDirection);
        return bestPoint;
    }

    void calculateMovesPoints() {
        LngLat currentPoint = approximatePoints.get(0);
        droneMovesPoints.add(currentPoint);
        for (int i = 0; i < approximatePoints.size() - 1; i++) {
            LngLat nextApproximatePoint = approximatePoints.get(i + 1);
            System.out.println("currentPoint = " + currentPoint);
            while (!currentPoint.closeTo(nextApproximatePoint)) {
                currentPoint = findNextPoint(currentPoint, nextApproximatePoint);
            }
//            /*
//             If the drone can't turn over the corner,
//             move one more time, this will guarantee the next point is visible.
//            */
//            if (i != approximatePoints.size() - 2 && !isVisible(currentPoint, approximatePoints.get(i + 1))) {
//                currentPoint = findNextPoint(currentPoint, nextApproximatePoint);
//            }
        }
    }

    public ArrayList<LngLat> getDroneMovesPoints() {
        return droneMovesPoints;
    }

    public ArrayList<CompassDirection> getDroneMovesDirections() {
        return droneMovesDirections;
    }
}
