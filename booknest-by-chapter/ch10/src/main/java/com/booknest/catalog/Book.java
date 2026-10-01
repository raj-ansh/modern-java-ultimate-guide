package com.booknest.catalog;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A product in the BookNest catalog. Immutable and always valid:
 * the title is never blank and the price is never negative.
 *
 * @param title the display title, without surrounding spaces
 * @param price the unit price
 */
public record Book(String title, BigDecimal price) {

    public Book {
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(price, "price must not be null");
        if (title.isBlank()) {
            throw new IllegalArgumentException(
                    "title must not be blank");
        }
        if (price.signum() < 0) {
            throw new IllegalArgumentException(
                    "price must not be negative: " + price);
        }
        title = title.strip();
    }

    /** Creates a book from a price written as text: "12.99". */
    public Book(String title, String price) {
        this(title, new BigDecimal(price));
    }

    /** Returns the price of the given number of copies. */
    public BigDecimal priceFor(int quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
