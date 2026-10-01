package com.booknest.cart;

import com.booknest.catalog.Book;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * One line of a cart: a book and how many copies of it.
 *
 * @param book     the product
 * @param quantity number of copies; always positive
 */
public record CartLine(Book book, int quantity) {

    public CartLine {
        Objects.requireNonNull(book, "book must not be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "quantity must be positive: " + quantity);
        }
    }

    /** Returns the price of this line. */
    public BigDecimal total() {
        return book.priceFor(quantity);
    }
}
