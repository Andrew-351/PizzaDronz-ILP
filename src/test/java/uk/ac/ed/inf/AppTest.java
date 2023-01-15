package uk.ac.ed.inf;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.Assert.*;

/**
 * Unit testing of argument validation (R1) and system testing for robustness (R4).
 */
public class AppTest {
    /*
        Unit tests of argument validation (R1)
     */

    @Test
    public void validateArgumentsNullTest() {
        assertFalse(App.validateArguments(null));
    }

    @Test
    public void validateArgumentsLessThanThreeTest() {
        String[] args = new String[2];
        args[0] = "2023-01-01";
        args[1] = "https://ilp-rest.azurewebsites.net";
        assertFalse(App.validateArguments(args));
    }

    @Test
    public void validateArgumentsMoreThanThreeTest() {
        String[] args = new String[4];
        args[0] = "2023-01-01";
        args[1] = "https://ilp-rest.azurewebsites.net";
        args[2] = "word";
        args[3] = "another word";
        assertFalse(App.validateArguments(args));
    }

    @Test
    public void validateArgumentsThreeCorrectTest() {
        String[] args = new String[3];
        args[0] = "2023-01-01";
        args[1] = "https://ilp-rest.azurewebsites.net";
        args[2] = "word";
        assertTrue(App.validateArguments(args));
    }

    @Test
    public void validateArgumentsIncorrectDateFormatTest() {
        String[] args = new String[3];
        args[0] = "2023-1-1";
        args[1] = "https://ilp-rest.azurewebsites.net";
        args[2] = "word";
        assertFalse(App.validateArguments(args));
    }

    @Test
    public void validateArgumentsInvalidDateTest() {
        String[] args = new String[3];
        args[0] = "2023-03-32";
        args[1] = "https://ilp-rest.azurewebsites.net";
        args[2] = "word";
        assertFalse(App.validateArguments(args));
    }

    @Test
    public void validateArgumentsInvalidUrlTest() {
        String[] args = new String[3];
        args[0] = "2023-11-21";
        args[1] = "https//ilp-rest.azurewebsites.net";
        args[2] = "word";
        assertFalse(App.validateArguments(args));
    }


    /*
        System robustness tests (R4)
     */
    private final PrintStream standardErr = System.err;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Test
    public void mainTestWrongArguments() {
        System.setErr(new PrintStream(outputStreamCaptor));

        String[] args = {"2023-1-9", "https://ilp-rest.azurewebsites.net", "random"};
        App.main(args);
        assertEquals("Invalid date format provided - must be YYYY-MM-DD.", outputStreamCaptor.toString().trim());

        System.setErr(standardErr);
    }

    @Test
    public void mainTestNoDataRestaurants() {
        System.setErr(new PrintStream(outputStreamCaptor));

        String[] args = {"2023-01-09", "https://ilp-rest-wrong.azurewebsites.net", "random"};
        App.main(args);
        assertEquals("No restaurants data retrieved from server.", outputStreamCaptor.toString().trim());

        System.setErr(standardErr);
    }

    @Test
    public void mainTestNoDataOrders() {
        System.setErr(new PrintStream(outputStreamCaptor));

        String[] args = {"2023-08-10", "https://ilp-rest.azurewebsites.net", "random"};
        App.main(args);
        assertEquals("No orders to deliver on 2023-08-10.", outputStreamCaptor.toString().trim());

        System.setErr(standardErr);
    }

    @Test
    public void mainTestSuccess() {
        System.setErr(new PrintStream(outputStreamCaptor));

        String[] args = {"2023-03-10", "https://ilp-rest.azurewebsites.net", "random"};
        App.main(args);
        assertEquals("", outputStreamCaptor.toString().trim());

        System.setErr(standardErr);
        assertTrue(new File("deliveries-2023-03-10.json").delete());
        assertTrue(new File("flightpath-2023-03-10.json").delete());
        assertTrue(new File("drone-2023-03-10.geojson").delete());
    }
}
