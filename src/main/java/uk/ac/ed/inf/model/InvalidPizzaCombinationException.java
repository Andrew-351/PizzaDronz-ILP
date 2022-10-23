package uk.ac.ed.inf.model;

/**
 * An exception thrown in case a combination of pizzas is invalid and cannot be delivered.
 */
public class InvalidPizzaCombinationException extends Exception {

    /**
     * Creates an instance of an InvalidPizzaCombinationException exception.
     * @param message message to be displayed
     */
    public InvalidPizzaCombinationException(String message)
    {
        super(message);
    }
}
