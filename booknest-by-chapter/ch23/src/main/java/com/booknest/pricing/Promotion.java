package com.booknest.pricing;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A promotion a customer applies at checkout with a code. Plain,
 * immutable data: the rules that price each kind live in
 * {@link Pricing#promotionDiscount}. The list of kinds is closed.
 */
public sealed interface Promotion {

    /** The code customers type, such as "SPRING10". */
    String code();

    /** No promotion: a Null Object, so checkout never needs null. */
    record NoPromotion() implements Promotion {
        @Override
        public String code() {
            return "NONE";
        }
    }

    /** A percentage off the subtotal. */
    record PercentOff(String code, int percent) implements Promotion {
        public PercentOff {
            Objects.requireNonNull(code, "code must not be null");
            if (percent < 1 || percent > 100) {
                throw new IllegalArgumentException(
                        "percent must be 1-100: " + percent);
            }
        }
    }

    /** A fixed amount off, once the subtotal reaches a minimum. */
    record AmountOff(String code, BigDecimal amount,
                     BigDecimal minimumSpend) implements Promotion {
        public AmountOff {
            Objects.requireNonNull(code, "code must not be null");
            if (amount.signum() <= 0 || minimumSpend.signum() < 0) {
                throw new IllegalArgumentException(
                        "invalid amounts: " + amount + ", "
                                + minimumSpend);
            }
        }
    }

    /** The cheapest item free, when buying at least some items. */
    record CheapestFree(String code, int minimumItems)
            implements Promotion {
        public CheapestFree {
            Objects.requireNonNull(code, "code must not be null");
            if (minimumItems < 2) {
                throw new IllegalArgumentException(
                        "needs at least 2 items: " + minimumItems);
            }
        }
    }
}
