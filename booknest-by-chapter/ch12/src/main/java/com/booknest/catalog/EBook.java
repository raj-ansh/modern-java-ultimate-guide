package com.booknest.catalog;

/**
 * A digital edition of a book. Delivered by download, never shipped.
 */
public final class EBook extends Book {

    public EBook(String title, String author, String price) {
        super(title, author, price);
    }

    @Override
    public boolean isShippable() {
        return false;
    }

    @Override
    public String menuLabel() {
        return super.menuLabel() + " (e-book)";
    }
}
