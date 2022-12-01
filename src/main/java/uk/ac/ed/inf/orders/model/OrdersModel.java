package uk.ac.ed.inf.orders.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * The Orders Model class is a model of the orders obtained on a certain date and everything related to them.
 */

public final class OrdersModel {
    /**
     * A LinkedHashMap which represents correspondence between the orders and their outcomes.
     */
    private final LinkedHashMap<Order, OrderOutcome> orderAndOutcomeMap = new LinkedHashMap<>();

    /**
     * A HashMap which represents correspondence between all restaurants and orders from them.
     */
    private final HashMap<Restaurant, ArrayList<Order>> restaurantAndOrdersMap = new HashMap<>();
    private final OrderValidator validator;
    private final ArrayList<Delivery> deliveries = new ArrayList<>();

    public OrdersModel(String date) throws NullPointerException {
        Restaurant[] restaurants = Restaurant.getRestaurantsFromRestServer();
        if (restaurants == null || restaurants.length == 0) {
            throw new NullPointerException("No restaurants data retrieved from server.");
        }
        for (var restaurant : restaurants) {
            restaurantAndOrdersMap.put(restaurant, new ArrayList<>());
        }

        Order[] orders = Order.getOrdersForDateFromRestServer(date);
        if (orders == null || orders.length == 0) {
            throw new NullPointerException("No orders to deliver on %s.".formatted(date));
        }
        for (var order : orders) {
            orderAndOutcomeMap.put(order, OrderOutcome.INVALID);
        }

        validator = new OrderValidator(restaurants);
    }


    /**
     * Performs validation of all orders.
     */
    public void validateOrders() {
        orderAndOutcomeMap.replaceAll((o, v) -> validator.validateOrder(o));
    }

    /**
     * For valid orders, associates them with the restaurant they are from.
     */
    public void allocateOrdersToRestaurants() {
        for (var order : orderAndOutcomeMap.keySet()) {
            if (orderAndOutcomeMap.get(order) == OrderOutcome.VALID_BUT_NOT_DELIVERED) {
                Restaurant restaurant = order.getRestaurantForOrder(restaurantAndOrdersMap.keySet());
                restaurantAndOrdersMap.get(restaurant).add(order);
            }
        }
    }

    /**
     * Returns a LinkedHashMap which represents associations between restaurants and orders from them.
     * All restaurants have at least one order from them.
     * @return associations of restaurants (which have orders) to the list of orders from them
     */
    public LinkedHashMap<Restaurant, ArrayList<Order>> getRestaurantsWithOrders() {
        LinkedHashMap<Restaurant, ArrayList<Order>> restaurantsWithOrders = new LinkedHashMap<>();
        for (var restaurant : restaurantAndOrdersMap.keySet()) {
            var orders = restaurantAndOrdersMap.get(restaurant);
            if (!orders.isEmpty()) {
                restaurantsWithOrders.put(restaurant, orders);
            }
        }
        return restaurantsWithOrders;
    }

    /**
     * Marks a given order as delivered.
     * @param order an order to be marked as delivered.
     */
    public void markOrderAsDelivered(Order order) {
        orderAndOutcomeMap.replace(order, OrderOutcome.DELIVERED);
    }

    /**
     * Records all deliveries made by the drone in the format required for the output file.
     */
    public void recordDeliveries() {
        for (var entry : orderAndOutcomeMap.entrySet()) {
            String orderNo = entry.getKey().orderNo();
            OrderOutcome outcome = entry.getValue();
            int cost = entry.getKey().priceTotalInPence();
            deliveries.add(new Delivery(orderNo, outcome, cost));
        }
    }

    public ArrayList<Delivery> getDeliveries() {
        return deliveries;
    }
}
