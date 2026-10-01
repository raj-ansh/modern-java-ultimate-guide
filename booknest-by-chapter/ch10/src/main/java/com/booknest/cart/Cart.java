package com.booknest.cart;

import com.booknest.catalog.Catalog;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A customer's shopping cart: how many of each catalog product.
 */
public class Cart {

    private final Catalog catalog;
    private final int[] quantities;

    public Cart(Catalog catalog) {
        this.catalog = Objects.requireNonNull(catalog);
        this.quantities = new int[catalog.size()];
    }

    /**
     * Adds copies of a product to the cart.
     *
     * @param product  catalog index of the product
     * @param quantity number of copies; must be positive
     * @throws IllegalArgumentException if either argument is invalid
     */
    public void add(int product, int quantity) {
        if (product < 0 || product >= quantities.length) {
            throw new IllegalArgumentException(
                    "no such product: " + product);
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "quantity must be positive: " + quantity);
        }
        quantities[product] += quantity;
    }

    /** Returns one line per product in the cart, in catalog order. */
    public CartLine[] lines() {
        CartLine[] lines = new CartLine[productsInCart()];
        int next = 0;
        for (int i = 0; i < quantities.length; i++) {
            if (quantities[i] > 0) {
                lines[next] =
                        new CartLine(catalog.get(i), quantities[i]);
                next++;
            }
        }
        return lines;
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
        for (CartLine line : lines()) {
            subtotal = subtotal.add(line.total());
        }
        return subtotal;
    }

    private int productsInCart() {
        int count = 0;
        for (int quantity : quantities) {
            if (quantity > 0) {
                count++;
            }
        }
        return count;
    }
}
