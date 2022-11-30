package uk.ac.ed.inf.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.orders.model.Delivery;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public final class OrdersView {
    public void createDeliveriesFile(String date, ArrayList<Delivery> deliveries) {
        try {
            String fileName = "deliveries-" + date + ".json";
            new ObjectMapper().writeValue(new FileOutputStream(fileName), deliveries);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
