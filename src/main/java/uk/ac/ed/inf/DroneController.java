package uk.ac.ed.inf;

import uk.ac.ed.inf.movement.MovementController;
import uk.ac.ed.inf.movement.model.Flightpath;
import uk.ac.ed.inf.orders.OrdersController;
import uk.ac.ed.inf.orders.model.Order;
import uk.ac.ed.inf.orders.model.Restaurant;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public final class DroneController {
    private final MovementController movementController;
    private final OrdersController ordersController;
    private final String date;
    private final String seed;

    private LinkedHashMap<Restaurant, ArrayList<Order>> restaurantsAndOrders;
    private LinkedHashMap<Restaurant, Flightpath> flightpathToRestaurants = new LinkedHashMap<>();

    public DroneController(String date, String serverBaseUrl, String seed) throws NullPointerException {
        RestServerClient.setBaseUrl(serverBaseUrl);
        this.date = date;
        this.seed = seed;
        movementController = new MovementController();
        ordersController = new OrdersController(date);
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
            flightpathToRestaurants.put(restaurant,
                    movementController.calculateShortestFlightpathToRestaurant(restaurant.getLocation()));
        }
    }

    private void sortRestaurantsByDistance() {
        int n = flightpathToRestaurants.size();

    }
}
