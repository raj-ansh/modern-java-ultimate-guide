package com.booknest.shop;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.booknest.cart.Cart;
import com.booknest.catalog.Book;
import com.booknest.catalog.Catalog;
import com.booknest.payment.Card;
import com.booknest.pricing.MembershipLevel;
import com.booknest.pricing.Promotion.CheapestFree;
import com.booknest.pricing.Promotion.NoPromotion;
import com.booknest.pricing.Promotion.PercentOff;
import com.booknest.pricing.Receipt;
import com.booknest.pricing.ShippingPolicy;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Possible only because ShopScreen writes to a stream it's given. */
class ShopScreenTest {

    private static final Book BOOK =
            new Book("The Quiet Library", "Asha Rao", "12.99");

    private final ByteArrayOutputStream shown =
            new ByteArrayOutputStream();
    private final ShopScreen screen = new ShopScreen(
            new PrintStream(shown, true, UTF_8), new Catalog(BOOK));

    private List<String> linesShown() {
        return shown.toString(UTF_8).lines().toList();
    }

    @Test
    void menuShowsProductsThenCommands() {
        screen.printMenu();

        assertEquals(List.of("",
                "1) The Quiet Library              $12.99",
                "7) Find an order",
                "8) Search",
                "9) View cart",
                "0) Checkout",
                "q) Checkout and close the shop"), linesShown());
    }

    @Test
    void receiptShowsEveryAmountAndThePayment() {
        Cart cart = new Cart();
        cart.add(BOOK, 2);
        Card card = new Card("4242");
        Receipt receipt = Receipt.of(cart, MembershipLevel.GOLD,
                new NoPromotion(), card, ShippingPolicy.standard());

        screen.printReceipt(receipt, MembershipLevel.GOLD,
                new NoPromotion(), card, "standard");

        assertEquals(List.of("",
                "Subtotal                   $25.98",
                "Discount (GOLD 15%)        -$3.90",
                "Tax (8.25%)                 $1.82",
                "Shipping (standard)         $4.99",
                "TOTAL                      $28.89",
                "Paid by card ending 4242."), linesShown());
    }

    @Test
    void describesPromotionsForCustomers() {
        assertEquals("SPRING10: 10% off",
                screen.describe(new PercentOff("SPRING10", 10)));
        assertEquals("BOOKMARK3: cheapest item free with 3+ items",
                screen.describe(new CheapestFree("BOOKMARK3", 3)));
    }
}
