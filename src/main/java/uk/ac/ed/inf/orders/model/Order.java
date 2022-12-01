package uk.ac.ed.inf.orders.model;

import uk.ac.ed.inf.RestServerClient;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * Representation of a pizza order.
 * @param orderNo a string which represents the order number
 * @param orderDate the date of the order
 * @param customer a string which represents the name of the customer
 * @param creditCardNumber the card number
 * @param creditCardExpiry the card expiry date
 * @param cvv the card CVV code
 * @param priceTotalInPence total cost of the order, including delivery fee
 * @param orderItems the names of the pizzas ordered
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

    /**
     * Returns a Restaurant instance from which this order was made if all pizzas of the order are on the menu
     * of the restaurant.
     * @param restaurants a collection of all restaurants taking part in the project
     * @return the restaurant from which this order is if it has all items of this order; null otherwise
     */
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
