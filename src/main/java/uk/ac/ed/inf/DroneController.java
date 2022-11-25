package uk.ac.ed.inf;

import uk.ac.ed.inf.movement.*;
import uk.ac.ed.inf.orders.*;
import java.util.ArrayList;
import java.util.HashMap;

public final class DroneController {
    private final MovementController movementController;
    private final OrdersController ordersController;
    private final String date;
    private final String seed;

    private HashMap<Restaurant, ArrayList<Order>> restaurantsAndOrders;
    private final HashMap<Restaurant, Flightpath> flightpathsToRestaurants = new HashMap<>();
    private final ArrayList<Restaurant> orderOfRestaurantsByDistance = new ArrayList<>();
    private final ArrayList<DroneMove> droneMoves = new ArrayList<>();
    private final long computationStart;

    public DroneController(String date, String serverBaseUrl, String seed) throws NullPointerException {
        RestServerClient.setBaseUrl(serverBaseUrl);
        this.date = date;
        this.seed = seed;
        movementController = new MovementController(date);
        ordersController = new OrdersController(date);
        computationStart = System.nanoTime();
//        f.constructVisibilityGraph();
//
//        System.out.print("     ");
//        for (int i = 0; i < 27; i++) {
//            System.out.print(String.format("%02d", i) + "  ");
//        }
//        System.out.println();
//        System.out.println("-".repeat(90));
//        double[][] visibilityGraph = f.getVisibilityGraph();
//        for (int j = 0; j < visibilityGraph.length; j++) {
//            System.out.print(String.format("%02d", j) + " | ");
//            double[] row = visibilityGraph[j];
//            for (int i = 0; i < row.length; i++) {
//                String s = ((Double) row[i]).toString();
//                System.out.print(s.substring(s.length() - 3) + " ");
//            }
//            System.out.println();
//        }
//        System.out.println(f.getAllPoints().size());
//        f.findShortestPath();
//        System.out.println(f.getFlightpathPoints());
//        f.optimiseFlightpath();
//        System.out.println(f.getFlightpathPoints());
//
//        f.calculateMovesPoints();
//        ArrayList<LngLat> points = f.getDroneMovesPoints();
//        ArrayList<CompassDirection> dirs = f.getDroneMovesDirections();
//        int n = points.size();
//        System.out.println(n);
//        System.out.println("\nMoves------------------");
//        for (int i = 0; i < n-1; i++) {
//            System.out.println(points.get(i) + " --> " + dirs.get(i));
//        }
//        System.out.println(points.get(n-1));
    }

    public void startSession() {
        ordersController.validateOrders();
        ordersController.allocateOrdersToRestaurants();
        restaurantsAndOrders = ordersController.getRestaurantsWithOrders();
        for (var restaurant : restaurantsAndOrders.keySet()) {
            flightpathsToRestaurants.put(
                    restaurant,
                    movementController.calculateShortestFlightpathToRestaurant(restaurant.getLocation())
            );
        }
        setOrderOfRestaurantsByDistance();
        deliverOrders();
        ordersController.setDeliveries();

        ordersController.outputDeliveries();
        movementController.outputDroneMoves(droneMoves);
    }

    private void setOrderOfRestaurantsByDistance() {
        HashMap<Restaurant, Integer> distancesToRestaurants = new HashMap<>();
        for (var restaurant : flightpathsToRestaurants.keySet()) {
            distancesToRestaurants.put(restaurant, flightpathsToRestaurants.get(restaurant).getDroneMovesDirections().size());
        }
        while (!distancesToRestaurants.isEmpty()) {
            Restaurant nextNearestRestaurant = null;
            int minDistance = Integer.MAX_VALUE;
            for (var entry : distancesToRestaurants.entrySet()) {
                if (entry.getValue() < minDistance) {
                    nextNearestRestaurant = entry.getKey();
                    minDistance = entry.getValue();
                }
            }
            orderOfRestaurantsByDistance.add(nextNearestRestaurant);
            distancesToRestaurants.remove(nextNearestRestaurant);
        }
    }

    private ArrayList<DroneMove> generateMovesForFlightpath(Flightpath flightpath, Order order) {
        ArrayList<DroneMove> droneMovesForFlightpath = new ArrayList<>();
        ArrayList<LngLat> movesPoints = new ArrayList<>(flightpath.getDroneMovesPoints());

        LngLat firstPoint = movesPoints.get(0);
        int pointsSize = movesPoints.size();
        for (int i = pointsSize - 1; i >= 0; i--) {
            movesPoints.add(flightpath.getDroneMovesPoints().get(i));
        }
        movesPoints.add(firstPoint);

        ArrayList<CompassDirection> movesDirections = new ArrayList<>(flightpath.getDroneMovesDirections());
        int directionsSize = movesDirections.size();
        movesDirections.add(CompassDirection.HOVER);
        for (int i = directionsSize - 1; i >= 0; i--) {
            movesDirections.add(flightpath.getDroneMovesDirections().get(i).getOppositeDirection());
        }
        movesDirections.add(CompassDirection.HOVER);

        for (int i = 0; i < movesDirections.size(); i++) {
            CompassDirection direction = movesDirections.get(i);
            Double angle = (direction == CompassDirection.HOVER) ? null : direction.getAngle();
            long ticks = System.nanoTime() - computationStart;
            droneMovesForFlightpath.add(new DroneMove(order.orderNo(),
                                         movesPoints.get(i).lng(),
                                         movesPoints.get(i).lat(),
                                         angle,
                                         movesPoints.get(i+1).lng(),
                                         movesPoints.get(i+1).lat(),
                                         ticks));
        }

        return droneMovesForFlightpath;
    }

    private void deliverOrders() {
        for (var restaurant : orderOfRestaurantsByDistance) {
            Flightpath flightpath = flightpathsToRestaurants.get(restaurant);
            ArrayList<Order> orders = restaurantsAndOrders.get(restaurant);
            for (var order : orders) {
                if (!movementController.enoughMovesLeft(flightpath)) {
                    return;
                }
                
                ArrayList<DroneMove> moves = generateMovesForFlightpath(flightpath, order);
                droneMoves.addAll(moves);
                movementController.makeDelivery(flightpath);
                ordersController.orderDelivered(order);
            }
        }
    }
}
