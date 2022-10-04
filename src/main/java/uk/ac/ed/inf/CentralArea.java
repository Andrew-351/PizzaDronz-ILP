package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper ;
import java.io .IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class CentralArea {
    public static CentralArea instance;
    public LngLat[] vertexCoordinates;

    CentralArea() throws IOException {
        vertexCoordinates = new ObjectMapper().readValue(
                new URL(Urls.BASE_URL + Urls.CENTRAL_AREA_ENDPOINT), LngLat[].class);
    }

    static {
        try {
            instance = new CentralArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
