package com.booknest.catalog;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Something BookNest sells. Every product is a specific kind (a
 * {@link Book} or an {@link Accessory}); the list is closed.
 * Immutable and always valid: the title is never blank, and the
 * price is never negative and has exactly two decimal places.
 */
public sealed abstract class Product permits Book, Accessory {

    private final String title;
    private final BigDecimal price;

    protected Product(String title, BigDecimal price) {
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(price, "price must not be null");
        if (title.isBlank()) {
            throw new IllegalArgumentException(
                    "title must not be blank");
        }
        if (price.signum() < 0 || price.scale() > 2) {
            throw new IllegalArgumentException(
                    "price must be 0 or more, in cents: " + price);
        }
        this.title = title.strip();
        this.price = price.setScale(2);
    }

    protected Product(String title, String price) {
        this(title, new BigDecimal(price));
    }

    public final String getTitle() {
        return title;
    }

    public final BigDecimal getPrice() {
        return price;
    }

    /** Returns the price of the given number of items. */
    public final BigDecimal priceFor(int quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    /** Whether this product has to be shipped to the customer. */
    public boolean isShippable() {
        return true;
    }

    /** The name shown in menus and on the cart. */
    public String menuLabel() {
        return title;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Product that = (Product) other;
        return title.equals(that.title) && price.equals(that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, price);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + title + ", "
                + price + "]";
    }
}
