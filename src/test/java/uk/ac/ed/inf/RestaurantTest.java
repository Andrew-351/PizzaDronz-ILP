package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for Restaurant class.
 */

public class RestaurantTest {
    @Test
    public void getRestaurantsFromRestServerTest() {
        try {
            assertNotNull(Restaurant.getRestaurantsFromRestServer(
                    new URL(RestServerClient.BASE_URL + RestServerClient.RESTAURANTS_ENDPOINT)));
        } catch (MalformedURLException e) {
            assert(false);
        }
    }
}
