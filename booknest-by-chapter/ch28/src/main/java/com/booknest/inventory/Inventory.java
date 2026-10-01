package com.booknest.inventory;

import com.booknest.catalog.Catalog;
import com.booknest.catalog.Product;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * How many items of each product are available. Thread-safe:
 * reserve() checks and updates the stock as one step, so two tills
 * can never sell the same last item.
 */
public class Inventory {

    private final Map<Product, Integer> available = new HashMap<>();

    /**
     * Creates an inventory with one stock level per catalog product.
     *
     * @throws IllegalArgumentException if the number of stock levels
     *         doesn't match the catalog, or a level is negative
     */
    public Inventory(Catalog catalog, int... stock) {
        Objects.requireNonNull(catalog);
        if (stock.length != catalog.size()) {
            throw new IllegalArgumentException("expected "
                    + catalog.size() + " stock levels, got "
                    + stock.length);
        }
        for (int i = 0; i < stock.length; i++) {
            if (stock[i] < 0) {
                throw new IllegalArgumentException(
                        "stock can't be negative: " + stock[i]);
            }
            available.put(catalog.get(i), stock[i]);
        }
    }

    /** Returns the stock level; 0 for a product never stocked. */
    public synchronized int available(Product product) {
        return available.getOrDefault(product, 0);
    }

    /**
     * Takes items out of stock for a customer's cart.
     *
     * @throws OutOfStockException if fewer items are available than
     *         requested; the stock is then left unchanged
     */
    public synchronized void reserve(Product product, int quantity)
            throws OutOfStockException {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "quantity must be positive: " + quantity);
        }
        int left = available(product);
        if (quantity > left) {
            throw new OutOfStockException(
                    product.menuLabel(), quantity, left);
        }
        available.put(product, left - quantity);
    }
}
