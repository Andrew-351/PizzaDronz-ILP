package uk.ac.ed.inf.orders;

import uk.ac.ed.inf.orders.model.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public final class OrdersController {
    private final OrdersModel ordersModel;
    private final OrdersView ordersView;

    public OrdersController(String date) throws NullPointerException {
        ordersModel = new OrdersModel(date);
        ordersView = new OrdersView();
    }

    public LinkedHashMap<Restaurant, ArrayList<Order>> getRestaurantsWithValidOrders() {
        ordersModel.validateOrders();
        ordersModel.allocateOrdersToRestaurants();
        return ordersModel.getRestaurantsWithOrders();
    }

    public void orderDelivered(Order order) {
        ordersModel.markOrderAsDelivered(order);
    }

    public void outputDeliveries(String date) {
        ordersModel.recordDeliveries();
        ordersView.createDeliveriesFile(date, ordersModel.getDeliveries());
    }
}
