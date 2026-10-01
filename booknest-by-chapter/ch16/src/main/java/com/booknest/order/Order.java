package com.booknest.order;

import com.booknest.common.Identifiable;
import com.booknest.pricing.MembershipLevel;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A completed order, as kept in the order history.
 *
 * @param id    the order number, such as "BN-1001"
 * @param level the customer's membership level
 * @param items number of items bought
 * @param total the amount paid
 */
public record Order(String id, MembershipLevel level, int items,
                    BigDecimal total)
        implements Identifiable<String> {

    public Order {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(level, "level must not be null");
        Objects.requireNonNull(total, "total must not be null");
        if (items <= 0) {
            throw new IllegalArgumentException(
                    "an order needs at least one item: " + items);
        }
    }
}
