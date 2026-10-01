package com.booknest.pricing;

import com.booknest.pricing.Promotion.AmountOff;
import com.booknest.pricing.Promotion.CheapestFree;
import com.booknest.pricing.Promotion.NoPromotion;
import com.booknest.pricing.Promotion.PercentOff;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The promotions currently on offer, found by code.
 */
public final class Promotions {

    private static final Map<String, Promotion> BY_CODE = Stream.of(
                    new NoPromotion(),
                    new PercentOff("SPRING10", 10),
                    new AmountOff("SAVE5", new BigDecimal("5.00"),
                            new BigDecimal("30.00")),
                    new CheapestFree("BOOKMARK3", 3))
            .collect(Collectors.toUnmodifiableMap(
                    Promotion::code, Function.identity()));

    private Promotions() {
        // static lookups only
    }

    /**
     * Finds a promotion by code, ignoring case and surrounding
     * spaces. "none" finds {@link NoPromotion}.
     */
    public static Optional<Promotion> find(String code) {
        return Optional.ofNullable(
                BY_CODE.get(code.strip().toUpperCase()));
    }
}
