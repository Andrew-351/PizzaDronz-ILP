package uk.ac.ed.inf.orders.model;

/**
 * Representation of a delivery in the format required for the output file.
 * @param orderNo a string representing the order number of a pizza order delivered/undelivered
 * @param outcome an outcome of the delivery (OrderOutcome object)
 * @param costInPence total cost of the order if it is valid
 */

public record Delivery(String orderNo, OrderOutcome outcome, int costInPence) {
}
