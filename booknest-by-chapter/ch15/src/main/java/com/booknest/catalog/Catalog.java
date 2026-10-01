package com.booknest.catalog;

import java.util.function.Predicate;

/**
 * The products BookNest sells, in menu order. Immutable.
 */
public final class Catalog {

    private final Product[] products;

    public Catalog(Product... products) {
        if (products.length == 0) {
            throw new IllegalArgumentException(
                    "a catalog needs at least one product");
        }
        this.products = products.clone();
    }

    /** Returns the number of products. */
    public int size() {
        return products.length;
    }

    /**
     * Returns the product at the given position.
     *
     * @param index a position from 0 to size() - 1
     */
    public Product get(int index) {
        return products[index];
    }

    /**
     * Returns the positions of all products that pass the test,
     * in catalog order.
     *
     * @param test decides whether a product matches
     */
    public int[] indexesMatching(Predicate<Product> test) {
        int count = 0;
        for (Product product : products) {
            if (test.test(product)) {
                count++;
            }
        }
        int[] matches = new int[count];
        int next = 0;
        for (int i = 0; i < products.length; i++) {
            if (test.test(products[i])) {
                matches[next] = i;
                next++;
            }
        }
        return matches;
    }
}
