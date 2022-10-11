package uk.ac.ed.inf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * PizzaDronz Project (Informatics Large Practical)
 *
 */
public class App 
{
    public static void main( String[] args ) {
//        CentralArea centralArea = CentralArea.instance;
//        System.out.println(new LngLat(-3.192473, 55.942617).inCentralArea());

        try {
            System.out.println(RestServerClient.getDataFromServer(new URL("https:lox.lt"), LngLat.class));
        } catch (MalformedURLException e) {
            throw new RuntimeException();
        }

        CentralArea centralArea = CentralArea.instance;
        System.out.println(new LngLat(-3.192473, 55.942617).inCentralArea());


        System.out.println("lox");
    }
}
