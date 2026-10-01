package com.booknest.catalog;

import java.util.List;
import java.util.function.Predicate;

/**
 * The products BookNest sells, in menu order. Immutable.
 */
public final class Catalog {

    private final List<Product> products;

    public Catalog(Product... products) {
        if (products.length == 0) {
            throw new IllegalArgumentException(
                    "a catalog needs at least one product");
        }
        // An unmodifiable copy; also rejects null products
        this.products = List.of(products);
    }

    /** Returns the number of products. */
    public int size() {
        return products.size();
    }

    /**
     * Returns the product at the given position.
     *
     * @param index a position from 0 to size() - 1
     */
    public Product get(int index) {
        return products.get(index);
    }

    /** Returns the product's position, or -1 if it isn't here. */
    public int indexOf(Product product) {
        return products.indexOf(product);
    }

    /** Returns the products that pass the test, in catalog order. */
    public List<Product> matching(Predicate<? super Product> test) {
        return products.stream()
                .filter(test)
                .toList();
    }
}
