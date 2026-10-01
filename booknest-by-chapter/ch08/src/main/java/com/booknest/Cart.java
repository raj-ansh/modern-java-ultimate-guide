package com.booknest;

import java.math.BigDecimal;

/**
 * A customer's shopping cart: how many of each catalog product.
 */
public class Cart {

    private final Book[] catalog;
    private final int[] quantities;

    public Cart(Book[] catalog) {
        this.catalog = catalog;
        this.quantities = new int[catalog.length];
    }

    public void add(int product, int quantity) {
        quantities[product] += quantity;
    }

    public int quantityOf(int product) {
        return quantities[product];
    }

    public int totalItems() {
        int total = 0;
        for (int quantity : quantities) {
            total += quantity;
        }
        return total;
    }

    public boolean isEmpty() {
        return totalItems() == 0;
    }

    public BigDecimal subtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (int i = 0; i < catalog.length; i++) {
            subtotal = subtotal.add(
                    catalog[i].priceFor(quantities[i]));
        }
        return subtotal;
    }
}
