package uk.ac.ed.inf.model;

import java.util.List;

/**
 * Representation of an order.
 */

public class Order {

    /**
     * Returns the cost (in pence) of delivering the specified list of pizzas by drone, including £1 delivery charge.
     * @param restaurants array of participating restaurants
     * @param pizzaNames list of names of the pizzas ordered
     * @return cost of delivering the pizzas if the combination of pizzas is valid;
     *         -1 if no restaurants/pizzas to be delivered are specified (i.e. one of the parameters is null or empty).
     * @throws InvalidPizzaCombinationException if the combination of pizzas is invalid
     */
    public int getDeliveryCost(Restaurant[] restaurants, List<String> pizzaNames) throws InvalidPizzaCombinationException {
        if (restaurants != null && restaurants.length > 0 && pizzaNames != null && pizzaNames.size() > 0) {
            if (pizzaNames.size() > 4) {
                throw new InvalidPizzaCombinationException("Invalid pizza combination - more than 4 pizzas ordered.");
            }
            for (Restaurant restaurant : restaurants) {
                int deliveryCost = 0;
                int pizzaNamesFound = 0;
                Menu.Pizza[] pizzas = restaurant.getMenu().getPizzas();
                for (String pizzaName : pizzaNames) {
                    boolean currentPizzaNameFound = false;
                    for (Menu.Pizza pizza : pizzas) {
                        if (pizza.name().equals(pizzaName)) {
                            deliveryCost += pizza.priceInPence();
                            pizzaNamesFound++;
                            currentPizzaNameFound = true;
                            break;
                        }
                    }
                    if (!currentPizzaNameFound) break;
                }
                if (pizzaNamesFound == pizzaNames.size()) {
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
