package com.booknest.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.booknest.cart.Cart;
import com.booknest.catalog.Book;
import com.booknest.catalog.EBook;
import com.booknest.payment.Card;
import com.booknest.pricing.Promotion.NoPromotion;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/** Written before the feature: these tests define gift wrapping. */
class GiftWrapTest {

    private static final Book BOOK =
            new Book("The Quiet Library", "Asha Rao", "12.99");
    private static final EBook EBOOK =
            new EBook("Java for Everyone", "Dev Malhotra", "19.99");

    private static Receipt receipt(Cart cart, boolean giftWrap) {
        return Receipt.of(cart, MembershipLevel.NONE,
                new NoPromotion(), new Card("4242"),
                ShippingPolicy.standard(), giftWrap);
    }

    @Test
    void costsTwoFiftyWhenAsked() {
        Cart cart = new Cart();
        cart.add(BOOK, 1);

        assertEquals(new BigDecimal("2.50"),
                receipt(cart, true).giftWrap());
    }

    @Test
    void isNotTaxedAndIsAddedToTheTotal() {
        Cart cart = new Cart();
        cart.add(BOOK, 1);
        Receipt plain = receipt(cart, false);
        Receipt wrapped = receipt(cart, true);

        assertEquals(plain.tax(), wrapped.tax());
        assertEquals(plain.total().add(new BigDecimal("2.50")),
                wrapped.total());
    }

    @Test
    void eBooksAloneCannotBeWrapped() {
        Cart cart = new Cart();
        cart.add(EBOOK, 1);

        assertEquals(BigDecimal.ZERO, receipt(cart, true).giftWrap());
    }

    @Test
    void notAskedMeansNoFee() {
        Cart cart = new Cart();
        cart.add(BOOK, 1);

        assertEquals(BigDecimal.ZERO,
                receipt(cart, false).giftWrap());
    }
}
