package uk.ac.ed.inf;

import uk.ac.ed.inf.movement.model.DroneMove;
import uk.ac.ed.inf.movement.model.Flightpath;
import uk.ac.ed.inf.orders.model.Order;
import uk.ac.ed.inf.orders.model.Restaurant;
import java.util.ArrayList;
import java.util.HashMap;

public final class DroneModel {
    private final String date;

    /**
     * Time in nanoseconds when the DroneController is initialised.
     */
    private final long computationStartTime;

    /**
     * A hashmap which represents correspondence between the restaurants and all valid orders from them.
     * The restaurants only include those from which there are orders on a given date.
     * The orders are all valid.
     */
    private HashMap<Restaurant, ArrayList<Order>> restaurantsAndOrders;

    /**
     * A hashmap which represents correspondence between the restaurants and flightpaths to them.
     * Only contains restaurants from which there are orders on a given date.
     */
    private final HashMap<Restaurant, Flightpath> flightpathsToRestaurants = new HashMap<>();

    /**
     * A list of restaurants starting from the nearest to the delivery point (Appleton Tower).
     */
    private final ArrayList<Restaurant> orderOfRestaurantsByDistance = new ArrayList<>();

    /**
     * A list of moves the drone will make delivering the orders on a given date.
     */
    private final ArrayList<DroneMove> droneMoves = new ArrayList<>();

    DroneModel(String date) throws NullPointerException {
        this.date = date;
        computationStartTime = System.nanoTime();
    }


    void recordFlightpathToRestaurant(Restaurant restaurant, Flightpath flightpath) {
        flightpathsToRestaurants.put(restaurant, flightpath);
    }

    void recordDeliveryMoves(ArrayList<DroneMove> moves) {
        droneMoves.addAll(moves);
    }

    /**
     * Creates a list of restaurants sorted by the distance from the delivery point.
     */
    void sortRestaurantsByDistance() {
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


    //////////////////////////////////////////////////////////////////////////////////////////////
    /////                                SETTERS and GETTERS                                 /////
    //////////////////////////////////////////////////////////////////////////////////////////////

    void setRestaurantsAndOrders(HashMap<Restaurant, ArrayList<Order>> restaurantsAndOrders) {
        this.restaurantsAndOrders = restaurantsAndOrders;
    }

    HashMap<Restaurant, ArrayList<Order>> getRestaurantsAndOrders() {
        return restaurantsAndOrders;
    }

    String getDate() {
        return date;
    }

    long getComputationStartTime() {
        return computationStartTime;
    }

    HashMap<Restaurant, Flightpath> getFlightpathsToRestaurants() {
        return flightpathsToRestaurants;
    }

    ArrayList<Restaurant> getOrderOfRestaurantsByDistance() {
        return orderOfRestaurantsByDistance;
    }

    ArrayList<DroneMove> getDroneMoves() {
        return droneMoves;
    }
}
