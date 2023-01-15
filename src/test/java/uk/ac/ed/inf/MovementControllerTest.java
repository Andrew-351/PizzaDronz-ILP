package uk.ac.ed.inf;

import org.junit.After;
import org.junit.Test;
import uk.ac.ed.inf.movement.MovementController;

/**
 * System robustness testing (R4) - MovementController instantiation.
 */

public class MovementControllerTest {
    @After
    public void resetBaseUrl() {
        RestServerClient.setBaseUrl("https://ilp-rest.azurewebsites.net/");
    }

    @Test
    public void orderControllerSuccessTest() throws NullPointerException {
        try {
            RestServerClient.setBaseUrl("https//ilp-rest.azurewebsites.net/");
            new MovementController();
            assert(true);   // does not throw an exception if no central area or no-fly zones retrieved
        } catch (NullPointerException e) {
            assert(false);
        }
    }
}
