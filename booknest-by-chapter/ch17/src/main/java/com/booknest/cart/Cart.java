package com.booknest.cart;

import com.booknest.catalog.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
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
        List<CartLine> lines = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry
                : quantities.entrySet()) {
            lines.add(new CartLine(entry.getKey(), entry.getValue()));
        }
        return List.copyOf(lines);
    }

    public int totalItems() {
        int total = 0;
        for (int quantity : quantities.values()) {
            total += quantity;
        }
        return total;
    }

    /** Returns how many items must be shipped (e-books are not). */
    public int shippableItems() {
        int total = 0;
        for (CartLine line : lines()) {
            if (line.product().isShippable()) {
                total += line.quantity();
            }
        }
        return total;
    }

    public boolean isEmpty() {
        return quantities.isEmpty();
    }

    public BigDecimal subtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartLine line : lines()) {
            subtotal = subtotal.add(line.total());
        }
        return subtotal;
    }
}
