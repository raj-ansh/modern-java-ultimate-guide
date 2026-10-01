package com.booknest.inventory;

import com.booknest.catalog.Catalog;
import java.util.Objects;

/**
 * How many items of each catalog product are available.
 */
public class Inventory {

    private final Catalog catalog;
    private final int[] available;

    /**
     * Creates an inventory with one stock level per catalog product.
     *
     * @throws IllegalArgumentException if the number of stock levels
     *         doesn't match the catalog, or a level is negative
     */
    public Inventory(Catalog catalog, int... stock) {
        this.catalog = Objects.requireNonNull(catalog);
        if (stock.length != catalog.size()) {
            throw new IllegalArgumentException("expected "
                    + catalog.size() + " stock levels, got "
                    + stock.length);
        }
        for (int level : stock) {
            if (level < 0) {
                throw new IllegalArgumentException(
                        "stock can't be negative: " + level);
            }
        }
        this.available = stock.clone();
    }

    public int available(int product) {
        return available[product];
    }

    /**
     * Takes items out of stock for a customer's cart.
     *
     * @throws OutOfStockException if fewer items are available than
     *         requested; the stock is then left unchanged
     */
    public void reserve(int product, int quantity)
            throws OutOfStockException {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "quantity must be positive: " + quantity);
        }
        int left = available[product];
        if (quantity > left) {
            throw new OutOfStockException(
                    catalog.get(product).menuLabel(), quantity, left);
        }
        available[product] = left - quantity;
    }
}
