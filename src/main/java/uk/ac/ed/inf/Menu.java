package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class Menu {
    public record Pizza(@JsonProperty("name") String name, @JsonProperty("priceInPence") int priceInPence) {}

    private final Pizza[] pizzas;

    public Menu(Map<String, Object>[] pizzas) {
        this.pizzas = new ObjectMapper().convertValue(pizzas, Pizza[].class);
    }

    public Pizza[] getPizzas() {
        return pizzas;
    }
}
