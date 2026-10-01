package com.booknest.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BookNest's pricing rules. No input, no output: only calculations.
 * All amounts are rounded half-up to cents.
 */
public final class Pricing {

    private static final BigDecimal TAX_RATE =
            new BigDecimal("0.0825");
    private static final BigDecimal SHIPPING_FEE =
            new BigDecimal("4.99");
    private static final int FREE_SHIPPING_ITEMS = 5;

    private Pricing() {
        // utility class: not meant to be instantiated
    }

    /** Returns the discount earned by the given membership level. */
    public static BigDecimal memberDiscount(BigDecimal subtotal,
                                            MembershipLevel level) {
        return toCents(subtotal.multiply(level.discountRate()));
    }

    /** Returns the 8.25% sales tax on the given amount. */
    public static BigDecimal tax(BigDecimal taxableAmount) {
        return toCents(taxableAmount.multiply(TAX_RATE));
    }

    /** Returns the shipping fee: free from five items upwards. */
    public static BigDecimal shipping(int itemsInCart) {
        if (itemsInCart >= FREE_SHIPPING_ITEMS) {
            return BigDecimal.ZERO;
        }
        return SHIPPING_FEE;
    }

    private static BigDecimal toCents(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
