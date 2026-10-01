package com.booknest.pricing;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A flat fee per order, free from a number of shippable items.
 *
 * @param fee           the fee for small orders
 * @param freeFromItems orders with this many items ship free
 */
public record StandardShipping(BigDecimal fee, int freeFromItems)
        implements ShippingPolicy {

    public StandardShipping {
        Objects.requireNonNull(fee, "fee must not be null");
        if (fee.signum() < 0 || freeFromItems < 1) {
            throw new IllegalArgumentException(
                    "invalid shipping rule");
        }
    }

    @Override
    public BigDecimal cost(int shippableItems) {
        if (shippableItems == 0 || shippableItems >= freeFromItems) {
            return BigDecimal.ZERO;
        }
        return fee;
    }

    @Override
    public String name() {
        return "standard";
    }
}
