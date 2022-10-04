package uk.ac.ed.inf;

public final class Urls {
    public static String BASE_URL = "https://ilp-rest.azurewebsites.net/";   // default base URL
    public static final String CENTRAL_AREA_ENDPOINT = "centralArea";
    public static final String NO_FLY_ZONES_ENDPOINT = "noFlyZones";
    public static final String RESTAURANTS = "restaurants";

    public static void setBaseUrl(String url) {
        BASE_URL = url;
    }
}
