package com.booknest.shop;

import com.booknest.catalog.Product;
import java.math.BigDecimal;
import java.util.function.Predicate;

/**
 * Turns what a customer types into a test for products.
 * Every word must appear in the product's label, and
 * "under 20" keeps only products cheaper than $20.
 * Example: "java under 25".
 */
public final class ProductSearch {

    private ProductSearch() {
        // static helpers only
    }

    public static Predicate<Product> parse(String text) {
        Predicate<Product> query = product -> true;
        String[] words = text.strip().toLowerCase().split("\\s+");

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (isPriceLimit(words, i)) {
                BigDecimal limit = new BigDecimal(words[i + 1]);
                query = query.and(product ->
                        product.getPrice().compareTo(limit) < 0);
                i++; // the number after "under" has been used
            } else {
                query = query.and(product -> product.menuLabel()
                        .toLowerCase().contains(word));
            }
        }
        return query;
    }

    private static boolean isPriceLimit(String[] words, int i) {
        return words[i].equals("under")
                && i + 1 < words.length
                && words[i + 1].matches("\\d+(\\.\\d{1,2})?");
    }
}
