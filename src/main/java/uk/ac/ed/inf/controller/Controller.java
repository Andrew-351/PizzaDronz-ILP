package uk.ac.ed.inf.controller;

import uk.ac.ed.inf.model.*;

public class Controller {
    private final CentralArea centralArea;
    private final NoFlyZone[] noFlyZones;
    private final Restaurant[] restaurants;
    private final Order[] orders;
    private final LngLat deliveryPoint;

    public Controller(String date, String serverBaseUrl, String seed) throws NullPointerException {
        RestServerClient.setBaseUrl(serverBaseUrl);
        centralArea = CentralArea.getCentralAreaFromRestServer();
        if (centralArea.vertexCoordinates().length == 0) {
            throw new NullPointerException("No central area data retrieved from server.");
        }
        noFlyZones = NoFlyZone.getNoFlyZonesFromRestServer();
        if (noFlyZones == null) {
            throw new NullPointerException("No no-fly zones data retrieved from server.");
        }
        restaurants = Restaurant.getRestaurantsFromRestServer();
        if (restaurants == null || restaurants.length == 0) {
            throw new NullPointerException("No restaurants data retrieved from server.");
        }
        orders = Order.getOrdersForDateFromRestServer(date);
        if (orders == null || orders.length == 0) {
            throw new NullPointerException("No orders to deliver on " + date + ".");
        }
        deliveryPoint = MovementConstants.APPLETON_TOWER;
    }

    private Flightpath calculateShortestFlightpathToRestaurant(LngLat restaurant) {
        return null;
    }

    public void sortRestaurantsByDistance() {
        double[] distancesToRestaurants = new double[restaurants.length];
    }
}
