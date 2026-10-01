import java.math.BigDecimal;

/// Shipping rates for **BookNest** orders.
///
/// Rates depend only on the number of *shippable* items:
///
/// | Items | Rate    |
/// |-------|---------|
/// | 0     | free    |
/// | 1–4   | `$4.99` |
/// | 5+    | free    |
///
/// See [BigDecimal] for why money is never a `double`.
public final class ShippingRates {

    private static final BigDecimal FLAT_FEE = new BigDecimal("4.99");

    private ShippingRates() {
    }

    /// Returns the shipping cost for an order.
    ///
    /// @param shippableItems number of items that need shipping;
    ///        must not be negative
    /// @return the fee, or [BigDecimal#ZERO] when shipping is free
    /// @throws IllegalArgumentException if `shippableItems` is
    ///         negative
    public static BigDecimal cost(int shippableItems) {
        if (shippableItems < 0) {
            throw new IllegalArgumentException(
                    "negative item count: " + shippableItems);
        }
        boolean free = shippableItems == 0 || shippableItems >= 5;
        return free ? BigDecimal.ZERO : FLAT_FEE;
    }
}
