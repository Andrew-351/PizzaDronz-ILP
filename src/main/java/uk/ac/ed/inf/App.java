package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * PizzaDronz Project (Informatics Large Practical): drone-based pizza delivery system.
 * <p>
 * A single drone delivering pizzas to Appleton Tower from different restaurants in Edinburgh.
 */
public class App {
    public static void main(String[] args) {
        boolean argsCorrect = validateArguments(args);
        if (!argsCorrect) {
            return;
        }

        String date = args[0];
        String baseUrlString = args[1];
        if (!baseUrlString.endsWith("/")) {
            baseUrlString += "/";
        }
        try {
            DroneController droneController = new DroneController(date, baseUrlString);
            droneController.startSession();


        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Validates the command-line arguments passed into the application's main method.
     * For the arguments to be valid, they should be:
     * 1) a valid date (in format YYYY-MM-DD),
     * 2) server base address, where the data would be retrieved from,
     * 3) (optionally) any word
     *
     * @param args command line arguments to be validated
     * @return true if arguments are valid; false otherwise
     */
    private static boolean validateArguments(String[] args) {
        if (args == null || args.length < 2) {
            System.err.println("Please provide as arguments: date, server base URL, and (optionally) any word.");
            return false;
        }

        String date = args[0];
        String baseUrlString = args[1];
        try {
            // Verify the date.
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(date, formatter);

            // Verify the URL.
            new URL(baseUrlString);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format provided - must be YYYY-MM-DD.");
            return false;
        } catch (MalformedURLException e) {
            System.err.println("Invalid server base URL provided.");
            return false;
        }
        return true;
    }
}
