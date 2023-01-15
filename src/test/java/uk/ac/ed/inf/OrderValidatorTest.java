package uk.ac.ed.inf;

import org.junit.Before;
import org.junit.Test;
import uk.ac.ed.inf.orders.model.Order;
import uk.ac.ed.inf.orders.model.OrderOutcome;
import uk.ac.ed.inf.orders.model.OrderValidator;
import uk.ac.ed.inf.orders.model.Restaurant;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Unit and system testing of order validation.
 */

public class OrderValidatorTest {
    private Restaurant[] restaurants;
    private OrderValidator validator;

    @Before
    public void setup() {
        restaurants = new Restaurant[3];
        List<Map<String,  Object>> maps = new ArrayList<>();

        maps.add(new HashMap<>(){{put("name", "Margarita"); put("priceInPence", 1000);}});
        maps.add(new HashMap<>(){{put("name", "Vegan"); put("priceInPence", 1200);}});
        restaurants[0] = new Restaurant(-3.18, 55.9, maps.toArray(new Map[0]));
        maps.clear();

        maps.add(new HashMap<>(){{put("name", "Meat Lover"); put("priceInPence", 1400);}});
        maps.add(new HashMap<>(){{put("name", "Seafood"); put("priceInPence", 1500);}});
        restaurants[1] = new Restaurant(-3.19, 55.8, maps.toArray(new Map[0]));
        maps.clear();

        maps.add(new HashMap<>(){{put("name", "Calzone"); put("priceInPence", 1400);}});
        maps.add(new HashMap<>(){{put("name", "Triple Cheese"); put("priceInPence", 1300);}});
        maps.add(new HashMap<>(){{put("name", "Hawaiian"); put("priceInPence", 1300);}});
        maps.add(new HashMap<>(){{put("name", "Double Pepperoni"); put("priceInPence", 1200);}});
        maps.add(new HashMap<>(){{put("name", "Proper Pizza"); put("priceInPence", 1000);}});
        restaurants[2] = new Restaurant(-3.185, 55.85, maps.toArray(new Map[0]));

        validator = new OrderValidator(restaurants);
    }

    /*
        Unit tests to check if separate order components' validation is correct (R2).
     */

    @Test
    public void generalFieldsInvalidOrderNumberTest() {
        Order order1 = new Order(null, "2023-01-01", "Name", "4123987646513215",
                "11/23", "123", 1100, new String[]{"Margarita"});
        Order order2 = new Order("1234", "2023-01-01", "Name", "4123987646513215",
                "11/23", "123", 1100, new String[]{"Margarita"});
        Order order3 = new Order("1234AFEG", "2023-01-01", "Name", "4123987646513215",
                "11/23", "123", 1100, new String[]{"Margarita"});
        assertFalse(validator.areGeneralFieldsValid(order1));
        assertFalse(validator.areGeneralFieldsValid(order2));
        assertFalse(validator.areGeneralFieldsValid(order3));
    }

    @Test
    public void generalFieldsInvalidOrderDateTest() {
        Order order1 = new Order("1234AFEB", null, "Name", "4123987646513215",
                "11/23", "123", 1100, new String[]{"Margarita"});
        Order order2 = new Order("1234AFEB", "2023-1-1", "Name", "4123987646513215",
                "11/23", "123", 1100, new String[]{"Margarita"});
        Order order3 = new Order("1234AFEB", "2023-01-32", "Name", "4123987646513215",
                "11/23", "123", 1100, new String[]{"Margarita"});
        assertFalse(validator.areGeneralFieldsValid(order1));
        assertFalse(validator.areGeneralFieldsValid(order2));
        assertFalse(validator.areGeneralFieldsValid(order3));
    }

    @Test
    public void generalFieldsInvalidCustomerNameTest() {
        Order order1 = new Order("1234AFEB", "2023-01-01", null, "4123987646513215",
                "11/23", "123", 1100, new String[]{"Margarita"});
        Order order2 = new Order("1234AFEB", "2023-01-01", "A", "4123987646513215",
                "11/23", "123", 1100, new String[]{"Margarita"});
        assertFalse(validator.areGeneralFieldsValid(order1));
        assertFalse(validator.areGeneralFieldsValid(order2));
    }

    @Test
    public void generalFieldsValidTest() {
        Order order1 = new Order("1234AFEB", "2023-03-14", "Andy", "4123",
                "11/23", "123", 1100, new String[]{"Margarita"});
        Order order2 = new Order("8091FFFF", "2023-02-28", "Vu", "4123",
                "11/23", "123", 1100, new String[]{"Margarita"});
        Order order3 = new Order("DC599EA0", "2023-01-31", "Name", "4123",
                "11/23", "123", 1100, new String[]{"Margarita"});
        assertTrue(validator.areGeneralFieldsValid(order1));
        assertTrue(validator.areGeneralFieldsValid(order2));
        assertTrue(validator.areGeneralFieldsValid(order3));
    }

    @Test
    public void cardNumberInvalidFormatTest() {
        Order order1 = new Order("1234AFEB", "2023-03-14", "Andy", null,
                "11/23", "123", 1100, new String[]{"Margarita"});
        Order order2 = new Order("8091FFFF", "2023-02-28", "Vu", "4123",
                "11/23", "123", 1100, new String[]{"Margarita"});
        Order order3 = new Order("DC599EA0", "2023-01-31", "Name", "412301234567890A",
                "11/23", "123", 1100, new String[]{"Margarita"});
        assertFalse(validator.isCardNumberValid(order1));
        assertFalse(validator.isCardNumberValid(order2));
        assertFalse(validator.isCardNumberValid(order3));
    }

    @Test
    public void cardNumberNotVisaOrMastercardTest() {
        Order order1 = new Order("1234AFEB", "2023-03-14", "Andy", "5634123412345678",
                "11/23", "123", 1100, new String[]{"Margarita"});
        Order order2 = new Order("8091FFFF", "2023-02-28", "Vu", "2220123456781234",
                "11/23", "123", 1100, new String[]{"Margarita"});
        assertFalse(validator.isCardNumberValid(order1));
        assertFalse(validator.isCardNumberValid(order2));
    }

    @Test
    public void cardNumberInvalidChecksumTest() {
        Order order1 = new Order("1234AFEB", "2023-03-14", "Andy", "4243627087087425",
                "11/23", "123", 1100, new String[]{"Margarita"});
        Order order2 = new Order("8091FFFF", "2023-02-28", "Vu", "2720314441125068",
                "11/23", "123", 1100, new String[]{"Margarita"});
        assertFalse(validator.isCardNumberValid(order1));
        assertFalse(validator.isCardNumberValid(order2));
    }

    @Test
    public void cardNumberValidTest() {
        Order order1 = new Order("1234AFEB", "2023-03-14", "Andy", "4243627087087424",
                "11/23", "123", 1100, new String[]{"Margarita"});
        Order order2 = new Order("8091FFFF", "2023-02-28", "Vu", "2720314441125069",
                "11/23", "123", 1100, new String[]{"Margarita"});
        Order order3 = new Order("8091FFFF", "2023-02-28", "Vu", "5340519060997548",
                "11/23", "123", 1100, new String[]{"Margarita"});
        assertTrue(validator.isCardNumberValid(order1));
        assertTrue(validator.isCardNumberValid(order2));
        assertTrue(validator.isCardNumberValid(order3));
    }

    @Test
    public void expiryDateInvalidFormatTest() {
        Order order1 = new Order("1234AFEB", "2023-03-14", "Andy", "4243627087087424",
                null, "123", 1100, new String[]{"Margarita"});
        Order order2 = new Order("8091FFFF", "2023-02-28", "Vu", "2720314441125069",
                "23/11", "123", 1100, new String[]{"Margarita"});
        Order order3 = new Order("8091FFFF", "2023-02-28", "Vu", "5340519060997548",
                "2023-11-23", "123", 1100, new String[]{"Margarita"});
        assertFalse(validator.isExpiryDateValid(order1));
        assertFalse(validator.isExpiryDateValid(order2));
        assertFalse(validator.isExpiryDateValid(order3));
    }

    @Test
    public void expiryDateBeforeOrderDateTest() {
        Order order1 = new Order("1234AFEB", "2023-03-14", "Andy", "4243627087087424",
                "12/22", "123", 1100, new String[]{"Margarita"});
        Order order2 = new Order("8091FFFF", "2023-02-28", "Vu", "2720314441125069",
                "01/23", "123", 1100, new String[]{"Margarita"});
        Order order3 = new Order("8091FFFF", "2023-03-31", "Vu", "5340519060997548",
                "03/23", "123", 1100, new String[]{"Margarita"});
        assertFalse(validator.isExpiryDateValid(order1));
        assertFalse(validator.isExpiryDateValid(order2));
        assertFalse(validator.isExpiryDateValid(order3));
    }

    @Test
    public void expiryDateValidTest() {
        Order order1 = new Order("1234AFEB", "2023-03-14", "Andy", "4243627087087424",
                "12/24", "123", 1100, new String[]{"Margarita"});
        Order order2 = new Order("8091FFFF", "2023-02-28", "Vu", "2720314441125069",
                "03/23", "123", 1100, new String[]{"Margarita"});
        Order order3 = new Order("8091FFFF", "2023-03-30", "Vu", "5340519060997548",
                "03/23", "123", 1100, new String[]{"Margarita"});
        assertTrue(validator.isExpiryDateValid(order1));
        assertTrue(validator.isExpiryDateValid(order2));
        assertTrue(validator.isExpiryDateValid(order3));
    }

    @Test
    public void cvvInvalidFormatTest() {
        Order order1 = new Order("1234AFEB", "2023-03-14", "Andy", "4243627087087424",
                "12/24", null, 1100, new String[]{"Margarita"});
        Order order2 = new Order("8091FFFF", "2023-02-28", "Vu", "2720314441125069",
                "03/23", "1234", 1100, new String[]{"Margarita"});
        Order order3 = new Order("8091FFFF", "2023-03-30", "Vu", "5340519060997548",
                "03/23", "1A3", 1100, new String[]{"Margarita"});
        assertFalse(validator.isCvvValid(order1));
        assertFalse(validator.isCvvValid(order2));
        assertFalse(validator.isCvvValid(order3));
    }

    @Test
    public void cvvValidTest() {
        Order order1 = new Order("1234AFEB", "2023-03-14", "Andy", "4243627087087424",
                "12/24", "123", 1100, new String[]{"Margarita"});
        Order order2 = new Order("8091FFFF", "2023-02-28", "Vu", "2720314441125069",
                "03/23", "905", 1100, new String[]{"Margarita"});
        assertTrue(validator.isCvvValid(order1));
        assertTrue(validator.isCvvValid(order2));
    }

    @Test
    public void pizzaCountInvalidTest() {
        Order order1 = new Order("1234AFEB", "2023-03-14", "Andy", "4243627087087424",
                "12/24", "123", 1100, null);
        Order order2 = new Order("8091FFFF", "2023-02-28", "Vu", "2720314441125069",
                "03/23", "905", 1100, new String[]{"Margarita", "Meat Lover", "Vegan", "Seafood", "Calzone"});
        assertFalse(validator.isPizzaCountValid(order1));
        assertFalse(validator.isPizzaCountValid(order2));
    }

    @Test
    public void pizzaCountValidTest() {
        Order order1 = new Order("1234AFEB", "2023-03-14", "Andy", "4243627087087424",
                "12/24", "123", 1100, new String[]{"Some Pizza"});
        Order order2 = new Order("8091FFFF", "2023-02-28", "Vu", "2720314441125069",
                "03/23", "905", 1100, new String[]{"Meat Lover", "Vegan", "Seafood", "Calzone"});
        assertTrue(validator.isPizzaCountValid(order1));
        assertTrue(validator.isPizzaCountValid(order2));
    }

    @Test
    public void pizzaUndefinedTest() {
        Order order1 = new Order("1234AFEB", "2023-03-14", "Andy", "4243627087087424",
                "12/24", "123", 1100, new String[]{"Margarita Wrong"});
        Order order2 = new Order("8091FFFF", "2023-02-28", "Vu", "2720314441125069",
                "03/23", "905", 1100, new String[]{"Margarita", "Meat Lover", "Vegan", "Seafood", "Calzone "});
        assertFalse(validator.areAllPizzasDefined(order1));
        assertFalse(validator.areAllPizzasDefined(order2));
    }

    @Test
    public void allPizzasDefinedTest() {
        Order order1 = new Order("1234AFEB", "2023-03-14", "Andy", "4243627087087424",
                "12/24", "123", 1100, new String[]{"Hawaiian"});
        Order order2 = new Order("8091FFFF", "2023-02-28", "Vu", "2720314441125069",
                "03/23", "905", 1100, new String[]{"Double Pepperoni", "Proper Pizza", "Triple Cheese", "Calzone"});
        assertTrue(validator.areAllPizzasDefined(order1));
        assertTrue(validator.areAllPizzasDefined(order2));
    }

    @Test
    public void pizzasNotFromSameRestaurantTest() {
        Order order1 = new Order("1234AFEB", "2023-03-14", "Andy", "4243627087087424",
                "12/24", "123", 1100, new String[]{"Margarita", "Seafood"});
        Order order2 = new Order("8091FFFF", "2023-02-28", "Vu", "2720314441125069",
                "03/23", "905", 1100, new String[]{"Hawaiian", "Double Pepperoni", "Vegan", "Meat Lover"});
        assertNull(validator.areAllPizzasFromSameRestaurant(order1));
        assertNull(validator.areAllPizzasFromSameRestaurant(order2));
    }

    @Test
    public void pizzasFromSameRestaurantTest() {
        Order order1 = new Order("1234AFEB", "2023-03-14", "Andy", "4243627087087424",
                "12/24", "123", 1100, new String[]{"Margarita", "Vegan"});
        Order order2 = new Order("8091FFFF", "2023-02-28", "Vu", "2720314441125069",
                "03/23", "905", 1100, new String[]{"Hawaiian"});
        Order order3 = new Order("8091FFFF", "2023-02-28", "Vu", "2720314441125069",
                "03/23", "905", 1100, new String[]{"Hawaiian", "Double Pepperoni", "Calzone", "Proper Pizza"});
        assertNotNull(validator.areAllPizzasFromSameRestaurant(order1));
        assertNotNull(validator.areAllPizzasFromSameRestaurant(order2));
        assertNotNull(validator.areAllPizzasFromSameRestaurant(order3));
    }

    @Test
    public void totalInvalidTest() {
        Order order1 = new Order("1234AFEB", "2023-03-14", "Andy", "4243627087087424",
                "12/24", "123", 1000, new String[]{"Margarita"});
        Order order2 = new Order("8091FFFF", "2023-02-28", "Vu", "2720314441125069",
                "03/23", "905", 3700, new String[]{"Calzone", "Proper Pizza", "Hawaiian"});
        assertFalse(validator.isTotalValid(order1, restaurants[0]));
        assertFalse(validator.isTotalValid(order2, restaurants[2]));
    }

    @Test
    public void totalValidTest() {
        Order order1 = new Order("1234AFEB", "2023-03-14", "Andy", "4243627087087424",
                "12/24", "123", 1600, new String[]{"Seafood"});
        Order order2 = new Order("8091FFFF", "2023-02-28", "Vu", "2720314441125069",
                "03/23", "905", 3800, new String[]{"Calzone", "Proper Pizza", "Hawaiian"});
        assertTrue(validator.isTotalValid(order1, restaurants[1]));
        assertTrue(validator.isTotalValid(order2, restaurants[2]));
    }


    /*
        System tests to check if an order is valid or invalid (R2, partially R3 - for VALID_BUT_NOT_DELIVERED).
     */

    @Test
    public void validOrderTest1() {
        Order order = new Order("1234AFEB", "2023-03-14", "Andy", "4243627087087424",
                "12/24", "123", 1600, new String[]{"Seafood"});
        assertSame(OrderOutcome.VALID_BUT_NOT_DELIVERED, validator.validateOrder(order));
    }

    @Test
    public void validOrderTest2() {
        Order order = new Order("90EF1B4C", "2023-05-30", "Andy", "2221444804065372",
                "05/23", "485", 5300, new String[]{"Calzone", "Triple Cheese", "Hawaiian", "Double Pepperoni"});
        assertSame(OrderOutcome.VALID_BUT_NOT_DELIVERED, validator.validateOrder(order));
    }


    /*
        If the order is invalid, check if a correct outcome is associated with it (R3).
     */

    @Test
    public void invalidOrderGeneralFieldsTest() {
        Order order1 = new Order("1234FEB", "2023-03-14", "Andy", "4243627087087424",
                "12/24", "123", 1600, new String[]{"Seafood"});
        Order order2 = new Order("1234FEB9", "2023-4--3", "Andy", "4243627087087424",
                "12/24", "123", 1600, new String[]{"Seafood"});
        Order order3 = new Order("1234FEB5", "2023-03-14", "I", "4243627087087424",
                "12/24", "123", 1600, new String[]{"Seafood"});
        assertSame(OrderOutcome.INVALID, validator.validateOrder(order1));
        assertSame(OrderOutcome.INVALID, validator.validateOrder(order2));
        assertSame(OrderOutcome.INVALID, validator.validateOrder(order3));
    }

    @Test
    public void invalidOrderCardNumberTest() {
        Order order = new Order("1234FEB9", "2023-03-14", "Andy", "4243627087087425",
                "12/24", "123", 1600, new String[]{"Seafood"});
        assertSame(OrderOutcome.INVALID_CARD_NUMBER, validator.validateOrder(order));
    }

    @Test
    public void invalidOrderExpiryDateTest() {
        Order order = new Order("1234FEB9", "2023-04-30", "Vu", "4243627087087424",
                "04/23", "041", 1600, new String[]{"Seafood"});
        assertSame(OrderOutcome.INVALID_EXPIRY_DATE, validator.validateOrder(order));
    }

    @Test
    public void invalidOrderCvvTest() {
        Order order = new Order("1234FEB9", "2023-03-14", "Andy", "4243627087087424",
                "12/24", "8025", 1600, new String[]{"Seafood"});
        assertSame(OrderOutcome.INVALID_CVV, validator.validateOrder(order));
    }

    @Test
    public void invalidOrderPizzaCountTest() {
        Order order = new Order("1234FEB9", "2023-03-14", "Vu", "4243627087087424",
                "12/26", "243", 1600, new String[]{"P1", "P2", "P3", "Calzone", "Vegan"});
        assertSame(OrderOutcome.INVALID_PIZZA_COUNT, validator.validateOrder(order));
    }

    @Test
    public void invalidOrderPizzaUndefinedTest() {
        Order order = new Order("1234FEB9", "2023-02-24", "Tom", "5385635814313463",
                "09/24", "123", 1600, new String[]{"Random Name", "Margarita"});
        assertSame(OrderOutcome.INVALID_PIZZA_NOT_DEFINED, validator.validateOrder(order));
    }

    @Test
    public void invalidOrderPizzasNotFromSameRestaurantTest() {
        Order order = new Order("12CAFE49", "2023-02-24", "Andy", "5385635814313463",
                "09/24", "069", 2000, new String[]{"Triple Cheese", "Margarita"});
        assertSame(OrderOutcome.INVALID_PIZZA_COMBINATION_MULTIPLE_SUPPLIERS, validator.validateOrder(order));
    }

    @Test
    public void invalidOrderTotalTest() {
        Order order = new Order("1234FEB9", "2023-04-24", "Tom", "5385635814313463",
                "09/23", "123", 2200, new String[]{"Vegan", "Margarita"});
        assertSame(OrderOutcome.INVALID_TOTAL, validator.validateOrder(order));
    }
}
