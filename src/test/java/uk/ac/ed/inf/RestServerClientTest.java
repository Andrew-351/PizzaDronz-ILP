package uk.ac.ed.inf;

import org.junit.Test;
import uk.ac.ed.inf.model.CentralArea;
import uk.ac.ed.inf.model.LngLat;
import uk.ac.ed.inf.model.RestServerClient;
import uk.ac.ed.inf.model.Restaurant;
import static org.junit.Assert.*;

/**
 * Unit tests for RestServerClient class.
 */

public class RestServerClientTest {
    @Test
    public void setBaseUrlTest() {
        String originalBaseUrl = RestServerClient.BASE_URL;
        RestServerClient.setBaseUrl("newURL");
        assertEquals("newURL", RestServerClient.BASE_URL);
        RestServerClient.setBaseUrl(originalBaseUrl);
        assertEquals(originalBaseUrl, RestServerClient.BASE_URL);
    }

    @Test
    public void getDataFromServerTestFailure() {
        assertNull(RestServerClient.getDataFromServer(
                RestServerClient.BASE_URL + RestServerClient.ORDERS_ENDPOINT, Restaurant.class));
    }

    @Test
    public void getDataFromServerTestSuccess() {
        CentralArea centralArea = CentralArea.getCentralAreaFromRestServer();
        LngLat[] vertices = (LngLat[]) RestServerClient.getDataFromServer(
                RestServerClient.BASE_URL + RestServerClient.CENTRAL_AREA_ENDPOINT, LngLat[].class);
        assertNotNull(vertices);
        assertArrayEquals(centralArea.vertexCoordinates(), vertices);

    }
}
