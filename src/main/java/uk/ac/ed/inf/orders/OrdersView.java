package uk.ac.ed.inf.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.orders.model.Delivery;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The Orders View class is responsible for outputting resulting data related to the pizza orders.
 */

public final class OrdersView {
    /**
     * Creates/overwrites a file containing an array of JSON records which represent all deliveries the drone will make
     * on the given date. The deliveries include all valid/invalid, delivered/undelivered orders.
     * @param date the date on which the drone is delivering orders
     * @param deliveries a list of all deliveries for the given date
     */
    public void createDeliveriesFile(String date, ArrayList<Delivery> deliveries) {
        String fileName = "deliveries-%s.json".formatted(date);
        try {
            new ObjectMapper().writeValue(new FileOutputStream(fileName), deliveries);
        } catch (IOException e) {
            System.err.printf("Could not write %s.%n", fileName);
        }
    }
}
