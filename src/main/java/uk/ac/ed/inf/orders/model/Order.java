package uk.ac.ed.inf.orders.model;

import uk.ac.ed.inf.RestServerClient;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * Representation of an order.
 */
public record Order (String orderNo, String orderDate, String customer, String creditCardNumber,
                     String creditCardExpiry, String cvv, int priceTotalInPence, String[] orderItems){
    /**
     * Static method to get all orders for a specific date.
     */
    public static Order[] getOrdersForDateFromRestServer(String date) {
        return (Order[]) RestServerClient.getDataFromServer(
                RestServerClient.BASE_URL + RestServerClient.ORDERS_ENDPOINT + date, Order[].class);
    }

    public Restaurant getRestaurantForOrder(Collection<Restaurant> restaurants) {
        for (var restaurant : restaurants) {
            var pizzaNames = Arrays.stream(restaurant.getMenu().getPizzas()).map(Menu.Pizza::name);
            if (new HashSet<>(pizzaNames.toList()).containsAll(Arrays.asList(orderItems))) {
                return restaurant;
            }
        }
        return null;
    }
}
