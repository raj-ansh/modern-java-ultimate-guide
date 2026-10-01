package com.booknest.pricing;

import com.booknest.cart.Cart;
import com.booknest.payment.PaymentMethod;
import java.math.BigDecimal;

/**
 * Every amount on a customer's receipt. Immutable, so it can be
 * shared freely, even between threads.
 */
public record Receipt(BigDecimal subtotal, BigDecimal memberDiscount,
                      BigDecimal promotionDiscount, BigDecimal tax,
                      BigDecimal shipping, BigDecimal paymentFee,
                      BigDecimal total) {

    /** Prices a cart with BookNest's rules. */
    public static Receipt of(Cart cart, MembershipLevel level,
                             Promotion promotion,
                             PaymentMethod payment,
                             ShippingPolicy policy) {
        BigDecimal subtotal = cart.subtotal();
        BigDecimal member = Pricing.memberDiscount(subtotal, level);
        BigDecimal promo =
                Pricing.promotionDiscount(promotion, cart.lines());
        BigDecimal taxable = subtotal.subtract(member)
                .subtract(promo).max(BigDecimal.ZERO);
        BigDecimal tax = Pricing.tax(taxable);
        BigDecimal shipping = policy.cost(cart.shippableItems());
        BigDecimal fee = Pricing.paymentFee(payment);
        BigDecimal total = taxable.add(tax).add(shipping).add(fee);
        return new Receipt(subtotal, member, promo, tax, shipping,
                fee, total);
    }
}
