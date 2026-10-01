package com.booknest.catalog;

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
}
