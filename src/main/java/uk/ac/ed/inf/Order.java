package uk.ac.ed.inf;

import java.util.List;

public class Order {

    public int getDeliveryCost(Restaurant[] restaurants, List<String> pizzaNames) throws InvalidPizzaCombinationException {
        if (restaurants != null && restaurants.length > 0 && pizzaNames != null && pizzaNames.size() > 0) {
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
            System.err.println("No participating restaurants or no pizzas in the order specified.");
            return -1;
        }
    }
}
