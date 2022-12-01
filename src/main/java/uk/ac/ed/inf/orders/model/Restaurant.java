package uk.ac.ed.inf.orders.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ed.inf.RestServerClient;
import uk.ac.ed.inf.movement.model.LngLat;

import java.util.Map;

/**
 * Representation of a restaurant participating in the scheme.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Restaurant {
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
     * @param longitude longitude (first coordinate of location) of the restaurant
     * @param latitude latitude (second coordinate of location) of the restaurant
     * @param menu menu of the restaurant
     */
    public Restaurant(@JsonProperty("longitude") double longitude,
                      @JsonProperty("latitude") double latitude,
                      @JsonProperty("menu") Map<String, Object>[] menu) {
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

    public LngLat getLocation() {
        return location;
    }

    public Menu getMenu() {
        return menu;
    }
}
