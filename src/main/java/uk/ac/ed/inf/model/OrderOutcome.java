package uk.ac.ed.inf.model;

/**
 * Representation of an outcome of an order.
 */

public enum OrderOutcome {
    Delivered,
    ValidButNotDelivered,
    InvalidCardNumber,
    InvalidExpiryDate,
    InvalidCvv,
    InvalidTotal,
    InvalidPizzaNotDefined,
    InvalidPizzaCount,
    InvalidPizzaCombinationMultipleSuppliers,
    Invalid
    }
