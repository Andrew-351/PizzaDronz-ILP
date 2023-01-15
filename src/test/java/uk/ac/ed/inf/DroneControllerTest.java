package uk.ac.ed.inf;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * System robustness testing (R4) - DroneController instantiation.
 */

public class DroneControllerTest {
    @After
    public void resetBaseUrl() {
        RestServerClient.setBaseUrl("https://ilp-rest.azurewebsites.net/");
    }

    @Test
    public void droneControllerFailureTest1() {
        String wrongUrl = "https://ilp-rest-wrong.azurewebsites.net";
        var thrown = assertThrows(NullPointerException.class, () -> new DroneController("2023-01-15", wrongUrl));
        assertEquals("No restaurants data retrieved from server.", thrown.getMessage());
    }

    @Test
    public void droneControllerFailureTest2() {
        String url = "https://ilp-rest.azurewebsites.net/";
        var thrown = assertThrows(NullPointerException.class, () -> new DroneController("2023-07-01", url));
        assertEquals("No orders to deliver on 2023-07-01.", thrown.getMessage());
    }

    @Test
    public void droneControllerSuccessTest() throws NullPointerException {
        try {
            new DroneController("2023-04-12", "https://ilp-rest.azurewebsites.net/");
            assert(true);
        } catch (NullPointerException e) {
            assert(false);
        }
    }
}
