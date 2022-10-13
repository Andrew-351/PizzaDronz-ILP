package uk.ac.ed.inf;

import java.io.IOException;
import java.net.URL;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Unit tests for Restaurant class.
 */

public class RestaurantTest {
    @Test
    public void getRestaurantsFromRestServerTestSuccess() {
        try {
            assertNotNull(Restaurant.getRestaurantsFromRestServer(
                    new URL(RestServerClient.BASE_URL)));
        } catch (IOException e) {
            fail();
        }
    }
}
