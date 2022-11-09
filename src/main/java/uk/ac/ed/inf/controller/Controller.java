package uk.ac.ed.inf.controller;

import uk.ac.ed.inf.model.*;

import java.util.Arrays;

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
        if (noFlyZones == null || noFlyZones.length == 0) {
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

        System.out.println(restaurants[0].getName());
        Flightpath f = new Flightpath(deliveryPoint, restaurants[0].getLocation(), noFlyZones);
        f.constructVisibilityGraph();

        System.out.print("     ");
        for (int i = 0; i < 27; i++) {
            System.out.print(String.format("%02d", i) + "  ");
        }
        System.out.println();
        System.out.println("-".repeat(90));
        double[][] visibilityGraph = f.getVisibilityGraph();
        for (int j = 0; j < visibilityGraph.length; j++) {
            System.out.print(String.format("%02d", j) + " | ");
            double[] row = visibilityGraph[j];
            for (int i = 0; i < row.length; i++) {
                String s = ((Double) row[i]).toString();
                System.out.print(s.substring(s.length() - 3) + " ");
            }
            System.out.println();
        }
    }

    private void calculateShortestFlightpathToRestaurant(LngLat restaurant) {

    }

    public void sortRestaurantsByDistance() {
        double[] distancesToRestaurants = new double[restaurants.length];
    }
}
