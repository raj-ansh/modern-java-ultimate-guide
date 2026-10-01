package com.booknest.catalog;

/**
 * The products BookNest sells, in menu order. Immutable.
 */
public final class Catalog {

    private final Book[] books;

    public Catalog(Book... books) {
        if (books.length == 0) {
            throw new IllegalArgumentException(
                    "a catalog needs at least one book");
        }
        this.books = books.clone();
    }

    /** Returns the number of products. */
    public int size() {
        return books.length;
    }

    /**
     * Returns the product at the given position.
     *
     * @param index a position from 0 to size() - 1
     */
    public Book get(int index) {
        return books[index];
    }
}
