package com.booknest;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BookNest's pricing rules. No input, no output: only calculations.
 */
public class Pricing {

    static final BigDecimal MEMBER_DISCOUNT = new BigDecimal("0.10");
    static final BigDecimal TAX_RATE = new BigDecimal("0.0825");
    static final BigDecimal SHIPPING_FEE = new BigDecimal("4.99");
    static final int FREE_SHIPPING_ITEMS = 5;

    static BigDecimal memberDiscount(BigDecimal subtotal,
                                     boolean isMember) {
        if (!isMember) {
            return BigDecimal.ZERO;
        }
        return toCents(subtotal.multiply(MEMBER_DISCOUNT));
    }

    static BigDecimal tax(BigDecimal taxableAmount) {
        return toCents(taxableAmount.multiply(TAX_RATE));
    }

    static BigDecimal shipping(int itemsInCart) {
        if (itemsInCart >= FREE_SHIPPING_ITEMS) {
            return BigDecimal.ZERO;
        }
        return SHIPPING_FEE;
    }

    static BigDecimal toCents(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
