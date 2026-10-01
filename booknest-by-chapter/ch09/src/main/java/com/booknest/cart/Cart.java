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
        checkProduct(product);
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "quantity must be positive: " + quantity);
        }
        quantities[product] += quantity;
    }

    public int quantityOf(int product) {
        checkProduct(product);
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
        for (int i = 0; i < quantities.length; i++) {
            subtotal = subtotal.add(
                    catalog.get(i).priceFor(quantities[i]));
        }
        return subtotal;
    }

    private void checkProduct(int product) {
        if (product < 0 || product >= quantities.length) {
            throw new IllegalArgumentException(
                    "no such product: " + product);
        }
    }
}
