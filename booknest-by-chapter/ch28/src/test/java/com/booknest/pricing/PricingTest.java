package com.booknest.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.booknest.cart.Cart;
import com.booknest.catalog.Accessory;
import com.booknest.catalog.Book;
import com.booknest.pricing.Promotion.AmountOff;
import com.booknest.pricing.Promotion.CheapestFree;
import com.booknest.pricing.Promotion.NoPromotion;
import com.booknest.pricing.Promotion.PercentOff;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PricingTest {

    private static final Book BOOK =
            new Book("The Quiet Library", "Asha Rao", "12.99");
    private static final Accessory BOOKMARKS =
            new Accessory("Bookmark set", "3.35");

    @Test
    @DisplayName("tax is 8.25%, rounded half-up to cents")
    void taxRoundsHalfUp() {
        // 22.00 x 0.0825 = 1.815, which must round up to 1.82
        assertEquals(new BigDecimal("1.82"),
                Pricing.tax(new BigDecimal("22.00")));
    }

    @ParameterizedTest(name = "{0} saves {2} on {1}")
    @CsvSource({
            "NONE,   29.33, 0.00",
            "BRONZE, 29.33, 1.47",
            "GOLD,   29.33, 4.40",
            "GOLD,   0.00,  0.00"
    })
    void memberDiscount(MembershipLevel level, String subtotal,
                        String expected) {
        assertEquals(new BigDecimal(expected), Pricing.memberDiscount(
                new BigDecimal(subtotal), level));
    }

    @Test
    void noPromotionGivesNothing() {
        assertEquals(BigDecimal.ZERO,
                Pricing.promotionDiscount(new NoPromotion(),
                        cart(2, 1).lines()));
    }

    @Test
    void percentOffIsTakenFromTheSubtotal() {
        // 2 x 12.99 + 3.35 = 29.33; 10% = 2.933, rounded to 2.93
        assertEquals(new BigDecimal("2.93"),
                Pricing.promotionDiscount(new PercentOff("P10", 10),
                        cart(2, 1).lines()));
    }

    @ParameterizedTest(name = "{0} books, {1} bookmark sets -> {2}")
    @CsvSource({
            "2, 1, 5.00",      // 29.33 reaches the 25.00 minimum
            "1, 1, 0",         // 16.34 doesn't
            "0, 1, 0"
    })
    void amountOffNeedsTheMinimumSpend(int books, int bookmarks,
                                       String expected) {
        AmountOff save5 = new AmountOff("SAVE5",
                new BigDecimal("5.00"), new BigDecimal("25.00"));
        assertEquals(new BigDecimal(expected),
                Pricing.promotionDiscount(save5,
                        cart(books, bookmarks).lines()));
    }

    @ParameterizedTest(name = "{0} books, {1} bookmark sets -> {2}")
    @CsvSource({
            "2, 1, 3.35",      // 3 items: the bookmark set is free
            "3, 0, 12.99",     // 3 items, all books: one book is free
            "1, 1, 0"          // only 2 items
    })
    void cheapestItemIsFreeFromThreeItems(int books, int bookmarks,
                                          String expected) {
        assertEquals(new BigDecimal(expected),
                Pricing.promotionDiscount(new CheapestFree("B3", 3),
                        cart(books, bookmarks).lines()));
    }

    private static Cart cart(int books, int bookmarks) {
        Cart cart = new Cart();
        if (books > 0) {
            cart.add(BOOK, books);
        }
        if (bookmarks > 0) {
            cart.add(BOOKMARKS, bookmarks);
        }
        return cart;
    }
}
