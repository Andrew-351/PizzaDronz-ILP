package uk.ac.ed.inf;

import uk.ac.ed.inf.model.*;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

/**
 * PizzaDronz Project (Informatics Large Practical).
 *
 */
public class App 
{
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Wrong number of arguments - must be 3.");
            // can it be 2?
        }
        else {
            String day = args[0];
            String baseUrlString = args[1];
            String seed = args[2];
            URL baseUrl = null;
            try {
                baseUrl = new URL(baseUrlString);
                if (!baseUrlString.endsWith("/")) {
                    baseUrlString += "/";
                }
                RestServerClient.setBaseUrl(baseUrlString);
            } catch (IOException e) {
                System.err.println("Invalid base URL provided.");
            }

            CentralArea centralArea = CentralArea.getCentralAreaFromRestServer();
            System.out.println(Arrays.toString(centralArea.vertexCoordinates()));

            NoFlyZone[] noFlyZones = NoFlyZone.getNoFlyZonesFromRestServer();
            for (NoFlyZone noFlyZone : noFlyZones) {
                System.out.println(noFlyZone.getVertexCoordinates().length);
            }
//            Restaurant[] restaurants = null;
//            restaurants = Restaurant.getRestaurantsFromRestServer();
        }
    }
}
