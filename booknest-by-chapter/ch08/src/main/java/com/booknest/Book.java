package com.booknest;

import java.math.BigDecimal;

/**
 * A product in the BookNest catalog.
 */
public class Book {

    private final String title;
    private final BigDecimal price;

    public Book(String title, BigDecimal price) {
        this.title = title;
        this.price = price;
    }

    public Book(String title, String price) {
        this(title, new BigDecimal(price));
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    /** The price of the given number of copies. */
    public BigDecimal priceFor(int quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
