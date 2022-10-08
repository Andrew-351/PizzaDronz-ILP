package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class Restaurant {
    private final String name;
    private final LngLat location;
    private final Menu menu;

    public Restaurant(@JsonProperty("name") String name,
                      @JsonProperty("longitude") double longitude,
                      @JsonProperty("latitude") double latitude,
                      @JsonProperty("menu") Map<String, Object>[] menu) {
        this.name = name;
        this.location = new LngLat(longitude, latitude);
        this.menu = new Menu(menu);
    }

    static Restaurant[] getRestaurantsFromRestServer(URL url) {
        try {
            return (Restaurant[]) RestServerClient.getDataFromServer(url, Restaurant[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public LngLat getLocation() {
        return location;
    }

    public Menu getMenu() {
        return menu;
    }
}
