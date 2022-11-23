package uk.ac.ed.inf.orders.model;

public record Delivery(String orderNo, OrderOutcome outcome, int costInPence) {
}
