package com.booknest.inventory;

/**
 * Thrown when a customer asks for more items than are in stock.
 * Checked on purpose: callers must decide what to tell the
 * customer, because running out of stock is a normal event.
 */
public class OutOfStockException extends Exception {

    private final String productLabel;
    private final int requested;
    private final int available;

    public OutOfStockException(String productLabel, int requested,
                               int available) {
        super(describe(productLabel, requested, available));
        this.productLabel = productLabel;
        this.requested = requested;
        this.available = available;
    }

    public String getProductLabel() {
        return productLabel;
    }

    public int getRequested() {
        return requested;
    }

    public int getAvailable() {
        return available;
    }

    private static String describe(String label, int requested,
                                   int available) {
        if (available == 0) {
            return label + " is sold out";
        }
        return "only " + available + " x " + label
                + " left (you asked for " + requested + ")";
    }
}
