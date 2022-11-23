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


//    /**
//     * Returns the cost (in pence) of delivering the specified list of pizzas by drone, including £1 delivery charge.
//     * @param restaurants array of participating restaurants
//     * @return cost of delivering the pizzas if the combination of pizzas is valid;
//     *         -1 if no restaurants/pizzas to be delivered are specified (i.e. one of the parameters is null or empty).
//     */
//    public int getDeliveryCost(Restaurant[] restaurants) {
//        if (restaurants != null && restaurants.length > 0 && orderItems != null && orderItems.length > 0) {
//            for (var restaurant : restaurants) {
//                int deliveryCost = 0;
//                int orderItemsFound = 0;
//                Menu.Pizza[] pizzas = restaurant.getMenu().getPizzas();
//                for (String pizzaName : orderItems) {
//                    boolean currentPizzaNameFound = false;
//                    for (var pizza : pizzas) {
//                        if (pizza.name().equals(pizzaName)) {
//                            deliveryCost += pizza.priceInPence();
//                            orderItemsFound++;
//                            currentPizzaNameFound = true;
//                            break;
//                        }
//                    }
//                    if (!currentPizzaNameFound) break;
//                }
//                if (orderItemsFound == orderItems.length) {
//                    return deliveryCost + 100;
//                }
//            }
//        }
//        else {
//            return -1;
//        }
//        return -1;
//    }
}
