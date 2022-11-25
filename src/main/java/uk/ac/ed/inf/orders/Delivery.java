package uk.ac.ed.inf.orders;

public record Delivery(String orderNo, OrderOutcome outcome, int costInPence) {
}
