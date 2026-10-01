package com.booknest.payment;

/**
 * How a customer pays. The set of payment methods is closed:
 * every switch over a PaymentMethod must handle all of them.
 */
public sealed interface PaymentMethod permits Card, CashOnDelivery {
}
