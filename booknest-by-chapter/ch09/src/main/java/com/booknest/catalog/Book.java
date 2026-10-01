package com.booknest.catalog;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A product in the BookNest catalog. Instances are immutable and
 * always valid: the title is never blank and the price is never
 * negative.
 */
public final class Book {

    private final String title;
    private final BigDecimal price;

    /**
     * Creates a book.
     *
     * @param title the display title; surrounding spaces are removed
     * @param price the unit price; must not be negative
     * @throws IllegalArgumentException if the title is blank or the
     *         price is negative
     * @throws NullPointerException if an argument is null
     */
    public Book(String title, BigDecimal price) {
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
        this.title = title.strip();
        this.price = price;
    }

    /**
     * Creates a book from a price written as text, such as "12.99".
     */
    public Book(String title, String price) {
        this(title, new BigDecimal(price));
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    /** Returns the price of the given number of copies. */
    public BigDecimal priceFor(int quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
