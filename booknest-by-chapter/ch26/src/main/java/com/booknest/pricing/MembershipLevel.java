package com.booknest.pricing;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

/**
 * BookNest membership levels and the discount each one earns.
 */
public enum MembershipLevel {

    NONE("0.00"),
    BRONZE("0.05"),
    SILVER("0.10"),
    GOLD("0.15");

    private final BigDecimal discountRate;

    MembershipLevel(String discountRate) {
        this.discountRate = new BigDecimal(discountRate);
    }

    /** Returns the discount as a fraction, such as 0.10 for 10%. */
    public BigDecimal discountRate() {
        return discountRate;
    }

    /**
     * Finds the level with this name, ignoring case and surrounding
     * spaces: "gold" and " GOLD " both give GOLD.
     */
    public static Optional<MembershipLevel> parse(String text) {
        String name = text.strip();
        return Arrays.stream(values())
                .filter(level -> level.name().equalsIgnoreCase(name))
                .findFirst();
    }

    /** Returns the discount as a whole percentage, such as 10. */
    public int discountPercent() {
        return discountRate.movePointRight(2).intValue();
    }
}
