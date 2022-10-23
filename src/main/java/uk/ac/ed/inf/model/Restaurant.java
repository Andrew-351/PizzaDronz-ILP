package uk.ac.ed.inf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Representation of a restaurant participating in the scheme.
 */

public class Restaurant {
    /**
     * Name of the restaurant.
     */
    private final String name;

    /**
     * Location (LngLat point) of the restaurant.
     */
    private final LngLat location;

    /**
     * Menu of the restaurant.
     */
    private final Menu menu;

    /**
     * Creates an instance of a Restaurant class.
     * @param name name of the restaurant
     * @param longitude longitude (first coordinate of location) of the restaurant
     * @param latitude latitude (second coordinate of location) of the restaurant
     * @param menu menu of the restaurant
     */
    public Restaurant(@JsonProperty("name") String name,
                      @JsonProperty("longitude") double longitude,
                      @JsonProperty("latitude") double latitude,
                      @JsonProperty("menu") Map<String, Object>[] menu) {
        this.name = name;
        this.location = new LngLat(longitude, latitude);
        this.menu = new Menu(menu);
    }

    /**
     * Static method to get an array of all participating restaurants from server.
     * @return an array of participating restaurants.
     */
    public static Restaurant[] getRestaurantsFromRestServer() {
        return (Restaurant[]) RestServerClient.getDataFromServer(
                RestServerClient.BASE_URL + RestServerClient.RESTAURANTS_ENDPOINT, Restaurant[].class);
    }

    /**
     * Returns the name of the restaurant.
     * @return the name of the restaurant.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the location of the restaurant.
     * @return the location of the restaurant.
     */
    public LngLat getLocation() {
        return location;
    }

    /**
     * Returns the menu of the restaurant.
     * @return the menu of the restaurant.
     */
    public Menu getMenu() {
        return menu;
    }
}
