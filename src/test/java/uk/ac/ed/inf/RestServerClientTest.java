package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
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
        try {
            assertNull(RestServerClient.getDataFromServer(
                    new URL(RestServerClient.BASE_URL + RestServerClient.ORDERS_ENDPOINT), Restaurant.class));
        } catch (MalformedURLException e) {
            fail();
        }
    }

    @Test
    public void getDataFromServerTestSuccess() {
        CentralArea centralArea = CentralArea.instance;
        try {
            LngLat[] vertices = (LngLat[]) RestServerClient.getDataFromServer(
                    new URL(RestServerClient.BASE_URL + RestServerClient.CENTRAL_AREA_ENDPOINT), LngLat[].class);
            assertNotNull(vertices);
            assertArrayEquals(centralArea.getVertexCoordinates(), vertices);

        } catch (MalformedURLException e) {
            fail();
        }
    }
}
