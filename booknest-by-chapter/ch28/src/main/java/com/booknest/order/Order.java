package com.booknest.order;

import com.booknest.cart.CartLine;
import com.booknest.common.Identifiable;
import com.booknest.pricing.MembershipLevel;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * A completed order, as kept in the order history.
 *
 * @param id       the order number, such as "BN-1001"
 * @param level    the customer's membership level
 * @param lines    what was bought; never empty, and never changes
 * @param total    the amount paid
 * @param placedAt when the order was placed: a moment on the UTC
 *                 timeline, shown in the store's time zone
 */
public record Order(String id, MembershipLevel level,
                    List<CartLine> lines, BigDecimal total,
                    Instant placedAt)
        implements Identifiable<String> {

    public Order {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(level, "level must not be null");
        Objects.requireNonNull(lines, "lines must not be null");
        Objects.requireNonNull(total, "total must not be null");
        Objects.requireNonNull(placedAt, "placedAt must not be null");
        lines = List.copyOf(lines);   // the caller's list may change
        if (lines.isEmpty()) {
            throw new IllegalArgumentException(
                    "an order needs at least one line");
        }
    }

    /** Returns the number of items bought. */
    public int items() {
        return lines.stream()
                .mapToInt(CartLine::quantity)
                .sum();
    }
}
