package com.booknest.cart;

import com.booknest.catalog.Product;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A customer's shopping cart: how many of each product.
 */
public class Cart {

    // LinkedHashMap: products stay in the order they were first added
    private final Map<Product, Integer> quantities =
            new LinkedHashMap<>();

    /**
     * Adds items of a product to the cart.
     *
     * @param quantity number of items; must be positive
     * @throws IllegalArgumentException if the quantity is invalid
     */
    public void add(Product product, int quantity) {
        Objects.requireNonNull(product, "product must not be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "quantity must be positive: " + quantity);
        }
        quantities.merge(product, quantity, Integer::sum);
    }

    /**
     * Returns one line per product, in the order first added.
     * The list is unmodifiable.
     */
    public List<CartLine> lines() {
        return quantities.entrySet().stream()
                .map(entry -> new CartLine(entry.getKey(),
                        entry.getValue()))
                .toList();
    }

    public int totalItems() {
        return quantities.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    /** Returns how many items must be shipped (e-books are not). */
    public int shippableItems() {
        return lines().stream()
                .filter(line -> line.product().isShippable())
                .mapToInt(CartLine::quantity)
                .sum();
    }

    public boolean isEmpty() {
        return quantities.isEmpty();
    }

    public BigDecimal subtotal() {
        return lines().stream()
                .map(CartLine::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
