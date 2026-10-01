package com.booknest.pricing;

import java.math.BigDecimal;

/**
 * A promotion: every order ships free.
 */
public record FreeShippingPromotion() implements ShippingPolicy {

    @Override
    public BigDecimal cost(int shippableItems) {
        return BigDecimal.ZERO;
    }

    @Override
    public String name() {
        return "promotion";
    }
}
