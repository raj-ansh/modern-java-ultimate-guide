package com.booknest.shop;

import com.booknest.cart.CartLine;
import java.util.Comparator;

/**
 * Orders cart lines for receipts: the biggest line total first,
 * ties broken alphabetically by label.
 */
public class MostExpensiveFirst implements Comparator<CartLine> {

    @Override
    public int compare(CartLine first, CartLine second) {
        int byTotal = second.total().compareTo(first.total());
        if (byTotal != 0) {
            return byTotal;
        }
        return first.product().menuLabel()
                .compareTo(second.product().menuLabel());
    }
}
