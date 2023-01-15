package uk.ac.ed.inf;

import org.junit.After;
import org.junit.Test;
import uk.ac.ed.inf.orders.OrdersController;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * System robustness testing (R4) - OrdersController instantiation.
 */

public class OrdersControllerTest {
    @After
    public void resetBaseUrl() {
        RestServerClient.setBaseUrl("https://ilp-rest.azurewebsites.net/");
    }

    @Test
    public void orderControllerFailureTest1() {
        RestServerClient.setBaseUrl("https//ilp-rest.azurewebsites.net");
        var thrown = assertThrows(NullPointerException.class, () -> new OrdersController("2023-01-15"));
        assertEquals("No restaurants data retrieved from server.", thrown.getMessage());
    }

    @Test
    public void orderControllerFailureTest2() {
        var thrown = assertThrows(NullPointerException.class, () -> new OrdersController("2023-06-30"));
        assertEquals("No orders to deliver on 2023-06-30.", thrown.getMessage());
    }

    @Test
    public void orderControllerSuccessTest() throws NullPointerException {
        try {
            new OrdersController("2023-03-30");
            assert(true);
        } catch (NullPointerException e) {
            assert(false);
        }
    }
}
