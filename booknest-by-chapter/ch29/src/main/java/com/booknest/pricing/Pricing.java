package com.booknest.pricing;

import com.booknest.cart.CartLine;
import com.booknest.payment.Card;
import com.booknest.payment.CashOnDelivery;
import com.booknest.payment.PaymentMethod;
import com.booknest.pricing.Promotion.AmountOff;
import com.booknest.pricing.Promotion.CheapestFree;
import com.booknest.pricing.Promotion.NoPromotion;
import com.booknest.pricing.Promotion.PercentOff;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

/**
 * BookNest's pricing rules. No input, no output: only calculations.
 * All amounts are rounded half-up to cents.
 */
public final class Pricing {

    private static final BigDecimal TAX_RATE =
            new BigDecimal("0.0825");
    private static final BigDecimal CASH_ON_DELIVERY_FEE =
            new BigDecimal("2.00");
    private static final BigDecimal GIFT_WRAP_FEE =
            new BigDecimal("2.50");

    private Pricing() {
        // utility class: not meant to be instantiated
    }

    /** Returns the discount earned by the given membership level. */
    public static BigDecimal memberDiscount(BigDecimal subtotal,
                                            MembershipLevel level) {
        return toCents(subtotal.multiply(level.discountRate()));
    }

    /**
     * Returns the discount a promotion gives on these cart lines.
     * A promotion whose condition isn't met gives nothing.
     */
    public static BigDecimal promotionDiscount(Promotion promotion,
                                               List<CartLine> lines) {
        BigDecimal subtotal = lines.stream()
                .map(CartLine::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int items = lines.stream().mapToInt(CartLine::quantity).sum();

        return switch (promotion) {
            case NoPromotion _ -> BigDecimal.ZERO;
            case PercentOff(_, int percent) -> toCents(subtotal
                    .multiply(BigDecimal.valueOf(percent))
                    .movePointLeft(2));
            case AmountOff(_, BigDecimal amount, BigDecimal minimum)
                    when subtotal.compareTo(minimum) >= 0 ->
                    amount.min(subtotal);
            case CheapestFree(_, int minimumItems)
                    when items >= minimumItems ->
                    cheapestPrice(lines);
            case AmountOff _, CheapestFree _ -> BigDecimal.ZERO;
        };
    }

    /** Returns the 8.25% sales tax on the given amount. */
    public static BigDecimal tax(BigDecimal taxableAmount) {
        return toCents(taxableAmount.multiply(TAX_RATE));
    }

    /** Returns the fee for the payment method: cards are free. */
    public static BigDecimal paymentFee(PaymentMethod method) {
        return switch (method) {
            case Card _ -> BigDecimal.ZERO;
            case CashOnDelivery _ -> CASH_ON_DELIVERY_FEE;
        };
    }

    /**
     * Returns the gift-wrap fee: $2.50 per order, only when wanted
     * and something will be shipped (an e-book can't be wrapped).
     * Like the payment fee, it's a service charge, so it isn't taxed.
     */
    public static BigDecimal giftWrapFee(boolean wanted,
                                         int shippableItems) {
        return wanted && shippableItems > 0
                ? GIFT_WRAP_FEE : BigDecimal.ZERO;
    }

    private static BigDecimal cheapestPrice(List<CartLine> lines) {
        return lines.stream()
                .map(line -> line.product().getPrice())
                .min(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);
    }

    private static BigDecimal toCents(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
