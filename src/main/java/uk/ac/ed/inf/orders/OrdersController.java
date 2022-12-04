package uk.ac.ed.inf.orders;

import uk.ac.ed.inf.orders.model.OrdersModel;
import uk.ac.ed.inf.orders.model.Order;
import uk.ac.ed.inf.orders.model.Restaurant;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * The Orders Controller class keeps track of and controls all actions solely related to the pizza orders.
 * It only directly communicates with Orders Model and Orders View.
 */

public final class OrdersController {
    private final OrdersModel ordersModel;
    private final OrdersView ordersView;

    public OrdersController(String date) throws NullPointerException {
        ordersModel = new OrdersModel(date);
        ordersView = new OrdersView();
    }

    /**
     * Returns a LinkedHashMap containing associations of restaurants to pizza orders from them.
     * All restaurants have at least one order from them. All orders are validated.
     * @return a LinkedHashMap of associations between restaurants and valid orders from them
     */
    public LinkedHashMap<Restaurant, ArrayList<Order>> getRestaurantsWithValidOrders() {
        ordersModel.validateOrders();
        ordersModel.allocateOrdersToRestaurants();
        return ordersModel.getRestaurantsWithOrders();
    }

    /**
     * Makes a record that the given order has been delivered.
     * @param order the order to be recorded as delivered
     */
    public void orderDelivered(Order order) {
        ordersModel.markOrderAsDelivered(order);
    }

    /**
     * Uses the Orders View to output the resulting data related to deliveries on a given date.
     * @param date the date on which the drone is delivering orders
     */
    public void outputDeliveries(String date) {
        ordersModel.recordDeliveries();
        ordersView.createDeliveriesFile(date, ordersModel.getDeliveries());
    }
}
