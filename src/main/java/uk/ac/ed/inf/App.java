package uk.ac.ed.inf;

import uk.ac.ed.inf.controller.Controller;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * PizzaDronz Project (Informatics Large Practical).
 *
 */
public class App 
{
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Wrong number of arguments - must be 3.");
        }
        else {
            String day = args[0];
            String baseUrlString = args[1];
            String seed = args[2];
            try {
                // Verify the day
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(day, formatter);

                // Verify the URL
                URL baseUrl = new URL(baseUrlString);
                if (!baseUrlString.endsWith("/")) {
                    baseUrlString += "/";
                }
                Controller controller = new Controller(day, baseUrlString, seed);
            } catch (DateTimeParseException e) {
                System.err.println("Invalid date format provided - must be YYYY-MM-DD.");
            } catch (MalformedURLException e) {
                System.err.println("Invalid server base URL provided.");
            } catch (NullPointerException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
