package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;

public final class RestServerClient {
    public static String BASE_URL = "https://ilp-rest.azurewebsites.net/";   // default base URL
    public final static String CENTRAL_AREA_ENDPOINT = "centralArea";
    public final static String NO_FLY_ZONES_ENDPOINT = "noFlyZones";
    public final static String RESTAURANTS_ENDPOINT = "restaurants";
    public final static String ORDERS_ENDPOINT = "orders";

    public static void setBaseUrl(String url) {
        BASE_URL = url;
    }

    public static Object getDataFromServer(URL url, Class<?> c) throws IOException {
        return new ObjectMapper().readValue(url, c);
    }
}
