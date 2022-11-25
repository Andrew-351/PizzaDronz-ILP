package uk.ac.ed.inf.orders;

import uk.ac.ed.inf.orders.view.DeliveriesView;

import java.util.*;

public final class OrdersController {
    private final String date;
    private final LinkedHashMap<Order, OrderOutcome> orderAndOutcomeMap = new LinkedHashMap<>();
    private final HashMap<Restaurant, ArrayList<Order>> restaurantAndOrdersMap = new HashMap<>();
    private final OrderValidator validator;
    private final ArrayList<Delivery> deliveries = new ArrayList<>();
    private final DeliveriesView deliveriesView = new DeliveriesView();

    public OrdersController(String date) throws NullPointerException {
        this.date = date;
        Restaurant[] restaurants = Restaurant.getRestaurantsFromRestServer();
        if (restaurants == null || restaurants.length == 0) {
            throw new NullPointerException("No restaurants data retrieved from server.");
        }
        for (var restaurant : restaurants) {
            restaurantAndOrdersMap.put(restaurant, new ArrayList<>());
        }

        Order[] orders = Order.getOrdersForDateFromRestServer(date);
        if (orders == null || orders.length == 0) {
            throw new NullPointerException("No orders to deliver on " + date + ".");
        }
        for (var order : orders) {
            orderAndOutcomeMap.put(order, OrderOutcome.Invalid);
        }

        validator = new OrderValidator(restaurants);
    }

    public void validateOrders() {
        orderAndOutcomeMap.replaceAll((o, v) -> validator.validateOrder(o));
    }

    public void allocateOrdersToRestaurants() {
        for (var order : orderAndOutcomeMap.keySet()) {
            if (orderAndOutcomeMap.get(order) == OrderOutcome.ValidButNotDelivered) {
                Restaurant restaurant = order.getRestaurantForOrder(restaurantAndOrdersMap.keySet());
                restaurantAndOrdersMap.get(restaurant).add(order);
            }
        }
    }

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

    public void orderDelivered(Order order) {
        orderAndOutcomeMap.replace(order, OrderOutcome.Delivered);
    }

    public void setDeliveries() {
        for (var entry : orderAndOutcomeMap.entrySet()) {
            String orderNo = entry.getKey().orderNo();
            OrderOutcome outcome = entry.getValue();
            int cost = entry.getKey().priceTotalInPence();

            deliveries.add(new Delivery(orderNo, outcome, cost));
        }
    }

    public void outputDeliveries() {
        deliveriesView.createDeliveriesFile(date, deliveries);
    }
}
