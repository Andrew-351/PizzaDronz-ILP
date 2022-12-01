package uk.ac.ed.inf;

import uk.ac.ed.inf.movement.model.DroneMove;
import uk.ac.ed.inf.movement.model.Flightpath;
import uk.ac.ed.inf.orders.model.Order;
import uk.ac.ed.inf.orders.model.Restaurant;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Drone Model class is a general model of the drone-based system.
 * Keeps information relevant for the drone that comes from different components of the system.
 */

public final class DroneModel {
    private final String date;
    private final long computationStartTime;

    /**
     * A HashMap which represents correspondence between the restaurants and all valid orders from them.
     * The restaurants only include those from which there are orders on a given date.
     * The orders are all valid.
     */
    private HashMap<Restaurant, ArrayList<Order>> restaurantsAndOrders;

    /**
     * A HashMap which represents correspondence between the restaurants and flightpaths to them.
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

    public DroneModel(String date) {
        this.date = date;
        computationStartTime = System.nanoTime();
    }

    /**
     * Makes a record of the association between a restaurant and the flightpath to it.
     * @param restaurant the restaurant
     * @param flightpath the flightpath to the restaurant
     */
    public void recordFlightpathToRestaurant(Restaurant restaurant, Flightpath flightpath) {
        flightpathsToRestaurants.put(restaurant, flightpath);
    }

    /**
     * Makes a record of the drone's moves necessary for a delivery.
     * @param moves the drone's moves necessary to make a delivery.
     */
    public void recordDeliveryMoves(ArrayList<DroneMove> moves) {
        droneMoves.addAll(moves);
    }

    /**
     * Creates a list of restaurants sorted by the distance from the delivery point.
     */
    public void sortRestaurantsByDistance() {
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

    public void setRestaurantsAndOrders(HashMap<Restaurant, ArrayList<Order>> restaurantsAndOrders) {
        this.restaurantsAndOrders = restaurantsAndOrders;
    }

    public HashMap<Restaurant, ArrayList<Order>> getRestaurantsAndOrders() {
        return restaurantsAndOrders;
    }

    public String getDate() {
        return date;
    }

    public long getComputationStartTime() {
        return computationStartTime;
    }

    public HashMap<Restaurant, Flightpath> getFlightpathsToRestaurants() {
        return flightpathsToRestaurants;
    }

    public ArrayList<Restaurant> getOrderOfRestaurantsByDistance() {
        return orderOfRestaurantsByDistance;
    }

    public ArrayList<DroneMove> getDroneMoves() {
        return droneMoves;
    }
}
