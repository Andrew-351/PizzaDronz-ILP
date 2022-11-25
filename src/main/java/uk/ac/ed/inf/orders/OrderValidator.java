package uk.ac.ed.inf.orders;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public final class OrderValidator {
    private final Restaurant[] restaurants;

    public OrderValidator(Restaurant[] restaurants) {
        this.restaurants = restaurants;
    }

    /**
     * Method to (in)validate an order
     * @param order the order
     * @return OrderOutcome which corresponds to the order
     */
    OrderOutcome validateOrder(Order order) {
        if (!areGeneralFieldsValid(order)) {
            return OrderOutcome.Invalid;
        }
        if (!isCardNumberValid(order)) {
            return OrderOutcome.InvalidCardNumber;
        }
        if (!isCvvValid(order)) {
            return OrderOutcome.InvalidCvv;
        }
        if (!isExpiryDateValid(order)) {
            return OrderOutcome.InvalidExpiryDate;
        }
        if (!isPizzaCountValid(order)) {
            return OrderOutcome.InvalidPizzaCount;
        }
        if (!areAllPizzasDefined(order)) {
            return OrderOutcome.InvalidPizzaNotDefined;
        }
        Restaurant restaurant = areAllPizzasFromSameRestaurant(order);
        if (restaurant == null) {
            return OrderOutcome.InvalidPizzaCombinationMultipleSuppliers;
        }
        if (!isTotalValid(order, restaurant)) {
            return OrderOutcome.InvalidTotal;
        }

        return OrderOutcome.ValidButNotDelivered;
    }

    private boolean isCardNumberValid(Order order) {
        String cardNumber = order.creditCardNumber();
        /*
            Check the card number contains only digits and its length is 16.
         */
        if (cardNumber == null || cardNumber.length() != 16 || !cardNumber.matches("^[0-9]+$")) {
            return false;
        }

        /*
            Check the card has a correct IIN range.
         */
        boolean correctIINRange = false;
        // Visa
        int firstDigits = Integer.parseInt(cardNumber.substring(0, 1));
        if (firstDigits == 4) {
            correctIINRange = true;
        }
        // MasterCard (51 - 55)
        if (!correctIINRange) {
            firstDigits = Integer.parseInt(cardNumber.substring(0, 2));
            if (51 <= firstDigits && firstDigits <= 55) {
                correctIINRange = true;
            }
        }
        // MasterCard (2221 - 2720)
        if (!correctIINRange) {
            firstDigits = Integer.parseInt(cardNumber.substring(0, 4));
            if (2221 <= firstDigits && firstDigits <= 2720) {
                correctIINRange = true;
            }
        }
        if (!correctIINRange) {
            return false;
        }

        /*
            Validate checksum using Luhn's algorithm (as explained here https://www.geeksforgeeks.org/luhn-algorithm/)
         */
        int sum = 0;
        boolean everySecond = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = cardNumber.charAt(i) - '0';
            if (everySecond) {
                digit *= 2;
            }
            sum += digit / 10;
            sum += digit % 10;
            everySecond = !everySecond;
        }

        return sum % 10 == 0;
    }

    private boolean isExpiryDateValid(Order order) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate orderDate = LocalDate.parse(order.orderDate(), dateFormatter);
        String expiryDateString = order.creditCardExpiry();
        if (expiryDateString == null) {
            return false;
        }
        try {
            DateTimeFormatter yearMonthFormatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expiryDate = YearMonth.parse(expiryDateString, yearMonthFormatter);
            return orderDate.isBefore(expiryDate.atEndOfMonth());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isCvvValid(Order order) {
        String cvv = order.cvv();
        return cvv != null && cvv.length() == 3 && cvv.matches("^[0-9]+$");
    }

    private boolean isPizzaCountValid(Order order) {
        if (order.orderItems() == null) {
            return false;
        }
        int numberOfPizzas = order.orderItems().length;
        return 0 < numberOfPizzas && numberOfPizzas <= 4;
    }

    private boolean areAllPizzasDefined(Order order) {
        for (var orderedPizzaName : order.orderItems()) {
            boolean currentPizzaNameFound = false;
            for (var restaurant : restaurants) {
                var pizzaNames = Arrays.stream(restaurant.getMenu().getPizzas()).map(Menu.Pizza::name);
                if (pizzaNames.anyMatch(orderedPizzaName::equals)) {
                    currentPizzaNameFound = true;
                    break;
                }
            }
            if (!currentPizzaNameFound) {
                return false;
            }
        }
        return true;
    }

    private Restaurant areAllPizzasFromSameRestaurant(Order order) {
        return order.getRestaurantForOrder(List.of(restaurants));
    }

    private boolean isTotalValid(Order order, Restaurant restaurant) {
        int realTotalCost = 100;    // delivery included
        for (var orderedPizzaName : order.orderItems()) {
            for (var pizza : restaurant.getMenu().getPizzas()) {
                if (pizza.name().equals(orderedPizzaName)) {
                    realTotalCost += pizza.priceInPence();
                    break;
                }
            }
        }
        return order.priceTotalInPence() == realTotalCost;
    }

    private boolean areGeneralFieldsValid(Order order) {
        if (order.customer() == null || order.customer().length() < 2) {
            return false;
        }
        if (order.orderNo() == null || order.orderNo().length() != 8 || !order.orderNo().matches("^[0-9A-Fa-f]+$")) {
            return false;
        }
        if (order.orderDate() == null) {
            return false;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(order.orderDate(), formatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
