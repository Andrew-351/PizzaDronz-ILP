package uk.ac.ed.inf.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

/**
 * Representation of a menu in a restaurant.
 */

public class Menu {

    /**
     * Representation of a pizza.
     * @param name name of the pizza
     * @param priceInPence price of the pizza in pence
     */
    public record Pizza(@JsonProperty("name") String name, @JsonProperty("priceInPence") int priceInPence) {}

    /**
     * Array of all pizzas on the menu.
     */
    private final Pizza[] pizzas;

    /**
     * Creates an instance of a Menu class.
     * @param pizzas an array of pizzas on this menu
     */
    public Menu(Map<String, Object>[] pizzas) {
        this.pizzas = new ObjectMapper().convertValue(pizzas, Pizza[].class);
    }

    /**
     * Returns an array of all pizzas on the menu.
     * @return an array of all pizzas on the menu.
     */
    public Pizza[] getPizzas() {
        return pizzas;
    }
}
