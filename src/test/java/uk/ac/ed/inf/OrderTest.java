package uk.ac.ed.inf;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import static org.junit.Assert.*;

/**
 * Unit tests for Order class.
 */


public class OrderTest {
    private Order order;
    private Restaurant[] restaurants;

    @Before
    public void setUp() {
        order = new Order();
        try {
            restaurants = Restaurant.getRestaurantsFromRestServer(
                    new URL(RestServerClient.BASE_URL + RestServerClient.RESTAURANTS_ENDPOINT));
        } catch (MalformedURLException e) {
            restaurants = null;
        }
    }

    @Test
    public void getDeliveryCostTestNoRestaurants() {
        Restaurant[] rs = new Restaurant[0];
        try {
            assertEquals(-1, order.getDeliveryCost(rs, new ArrayList<>(Collections.singleton("Pizza1"))));
        } catch (InvalidPizzaCombinationException e) {
            assert(false);
        }
    }

    @Test
    public void getDeliveryCostTestNoPizzaNames() {
        try {
            assertEquals(-1, order.getDeliveryCost(restaurants, new ArrayList<>()));
        } catch (InvalidPizzaCombinationException e) {
            assert(false);
        }
    }

    @Test
    public void getDeliveryCostTestTooManyPizzas() {
        ArrayList<String> pizzas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            pizzas.add("Pizza" + i);
        }
        try {
            order.getDeliveryCost(restaurants, pizzas);
            assert(false);
        } catch (InvalidPizzaCombinationException e) {
            assert(true);
        }
    }

    @Test
    public void getDeliveryCostTestInvalidCombination() {
        ArrayList<String> pizzas = new ArrayList<>();
        pizzas.add("Calzone");
        pizzas.add("Vegan Delight");
        try {
            order.getDeliveryCost(restaurants, pizzas);
            assert(false);
        } catch (InvalidPizzaCombinationException e) {
            assert(true);
        }
    }

    @Test
    public void getDeliveryCostTestValidCombinationOnePizza() {
        ArrayList<String> pizzas = new ArrayList<>();
        pizzas.add("Calzone");
        try {
            int cost = order.getDeliveryCost(restaurants, pizzas);
            assertEquals(1500, cost);
        } catch (InvalidPizzaCombinationException e) {
            assert(false);
        }
    }

    @Test
    public void getDeliveryCostTestValidCombinationTwoPizzas() {
        ArrayList<String> pizzas = new ArrayList<>();
        pizzas.add("Vegan Delight");
        pizzas.add("Meat Lover");
        try {
            int cost = order.getDeliveryCost(restaurants, pizzas);
            assertEquals(2600, cost);
        } catch (InvalidPizzaCombinationException e) {
            assert(false);
        }
    }
}
