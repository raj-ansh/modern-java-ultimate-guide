package com.booknest.pricing;

import java.math.BigDecimal;

/**
 * How shipping is charged. Implementations are interchangeable: the
 * checkout works with any policy. Marketing can add new promotions
 * by writing a new implementation, without touching the checkout.
 */
public interface ShippingPolicy {

    /**
     * Returns the shipping cost for an order.
     *
     * @param shippableItems items that must be shipped (0 or more)
     * @return the cost; zero means free
     */
    BigDecimal cost(int shippableItems);

    /** Returns a short name for receipts, such as "standard". */
    String name();

    /** $4.99; free from 5 shippable items or if nothing ships. */
    static ShippingPolicy standard() {
        return new StandardShipping(new BigDecimal("4.99"), 5);
    }

    /** Free shipping on every order. */
    static ShippingPolicy freeShippingPromotion() {
        return new FreeShippingPromotion();
    }
}
