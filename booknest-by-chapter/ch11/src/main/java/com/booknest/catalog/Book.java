package com.booknest.catalog;

import java.util.Objects;

/**
 * A printed book: a product with an author.
 */
public class Book extends Product {

    private final String author;

    public Book(String title, String author, String price) {
        super(title, price);
        Objects.requireNonNull(author, "author must not be null");
        if (author.isBlank()) {
            throw new IllegalArgumentException(
                    "author must not be blank");
        }
        this.author = author.strip();
    }

    public final String getAuthor() {
        return author;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other)
                && author.equals(((Book) other).author);
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + author.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + getTitle()
                + " by " + author + ", " + getPrice() + "]";
    }
}
