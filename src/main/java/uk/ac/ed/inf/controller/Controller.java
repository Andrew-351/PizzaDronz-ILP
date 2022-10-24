package uk.ac.ed.inf.controller;

import uk.ac.ed.inf.model.*;

public class Controller {
    private final CentralArea centralArea;
    private final NoFlyZone[] noFlyZones;
    private final Restaurant[] restaurants;
    private Order[] orders;

    public Controller(String date, String serverBaseUrl, String seed) {
        RestServerClient.setBaseUrl(serverBaseUrl);
        this.centralArea = CentralArea.getCentralAreaFromRestServer();
        this.noFlyZones = NoFlyZone.getNoFlyZonesFromRestServer();
        this.restaurants = Restaurant.getRestaurantsFromRestServer();
        this.orders = Order.getOrdersForDateFromRestServer(date);
    }
}
