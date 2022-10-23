package uk.ac.ed.inf.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;

/**
 * Rest Server Client to communicate with the server.
 */

public final class RestServerClient {
    /**
     * Default base URL of the server.
     */
    public static String BASE_URL = "https://ilp-rest.azurewebsites.net/";
    public final static String CENTRAL_AREA_ENDPOINT = "centralArea";
    public final static String NO_FLY_ZONES_ENDPOINT = "noFlyZones";
    public final static String RESTAURANTS_ENDPOINT = "restaurants";
    public final static String ORDERS_ENDPOINT = "orders";

    /**
     * Updates the base URL of the server.
     * @param url new base URL of the server
     */
    public static void setBaseUrl(String url) {
        BASE_URL = url;
    }

    /**
     * Universal method to get any data from server.
     * @param url URL where to get data from (an endpoint)
     * @param c variable type for deserialisation
     * @return data retrieved from server if successfully read; null otherwise.
     */
    public static Object getDataFromServer(String url, Class<?> c) {
        try {
            return new ObjectMapper().readValue(new URL(url), c);
        } catch (IOException e) {
            return null;
        }
    }
}
