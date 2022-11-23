package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * PizzaDronz Project (Informatics Large Practical).
 *
 */
public class App {
    public static void main(String[] args) {
        boolean argsCorrect = validateArguments(args);
        if (!argsCorrect) {
            return;
        }

        String date = args[0];
        String baseUrlString = args[1];
        String seed = args[2];
        if (!baseUrlString.endsWith("/")) {
            baseUrlString += "/";
        }
        try {
            DroneController droneController = new DroneController(date, baseUrlString, seed);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate orderDate = LocalDate.parse("2023-04-29", dateFormatter);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expiryDate = YearMonth.parse("04/23", formatter);
            System.out.println(orderDate.isBefore(expiryDate.atEndOfMonth()));


        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
        }
    }

    private static boolean validateArguments(String[] args) {
        if (args.length != 3) {
            System.err.println("Wrong number of arguments - must be 3.");
            return false;
        }

        String date = args[0];
        String baseUrlString = args[1];
        try {
            // Verify the date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(date, formatter);

            // Verify the URL
            URL baseUrl = new URL(baseUrlString);
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
