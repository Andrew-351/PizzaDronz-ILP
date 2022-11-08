package uk.ac.ed.inf.model;

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

    /**
     * Returns the cost (in pence) of delivering the specified list of pizzas by drone, including £1 delivery charge.
     * @param restaurants array of participating restaurants
     * @return cost of delivering the pizzas if the combination of pizzas is valid;
     *         -1 if no restaurants/pizzas to be delivered are specified (i.e. one of the parameters is null or empty).
     * @throws InvalidPizzaCombinationException if the combination of pizzas is invalid
     */
    public int getDeliveryCost(Restaurant[] restaurants) throws InvalidPizzaCombinationException {
        if (restaurants != null && restaurants.length > 0 && orderItems != null && orderItems.length > 0) {
            if (orderItems.length > 4) {
                throw new InvalidPizzaCombinationException("Invalid pizza combination - more than 4 pizzas ordered.");
            }
            for (var restaurant : restaurants) {
                int deliveryCost = 0;
                int orderItemsFound = 0;
                Menu.Pizza[] pizzas = restaurant.getMenu().getPizzas();
                for (String pizzaName : orderItems) {
                    boolean currentPizzaNameFound = false;
                    for (var pizza : pizzas) {
                        if (pizza.name().equals(pizzaName)) {
                            deliveryCost += pizza.priceInPence();
                            orderItemsFound++;
                            currentPizzaNameFound = true;
                            break;
                        }
                    }
                    if (!currentPizzaNameFound) break;
                }
                if (orderItemsFound == orderItems.length) {
                    return deliveryCost + 100;
                }
            }
            throw new InvalidPizzaCombinationException("Invalid pizza combination - pizzas are not from the same restaurant.");
        }
        else {
            return -1;
        }
    }
}
