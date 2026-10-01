package com.booknest.cart;

import com.booknest.catalog.Product;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * One line of a cart: a product and how many of it.
 *
 * @param product  the product
 * @param quantity number of items; always positive
 */
public record CartLine(Product product, int quantity) {

    public CartLine {
        Objects.requireNonNull(product, "product must not be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "quantity must be positive: " + quantity);
        }
    }

    /** Returns the price of this line. */
    public BigDecimal total() {
        return product.priceFor(quantity);
    }
}
