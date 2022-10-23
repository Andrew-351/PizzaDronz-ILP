package uk.ac.ed.inf;

import org.junit.Test;
import uk.ac.ed.inf.model.Restaurant;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for Restaurant class.
 */

public class RestaurantTest {
    @Test
    public void getRestaurantsFromRestServerTestSuccess() {
        assertNotNull(Restaurant.getRestaurantsFromRestServer());
    }
}
