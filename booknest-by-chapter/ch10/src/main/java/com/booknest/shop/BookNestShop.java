package com.booknest.shop;

import com.booknest.cart.Cart;
import com.booknest.cart.CartLine;
import com.booknest.catalog.Book;
import com.booknest.catalog.Catalog;
import com.booknest.pricing.MembershipLevel;
import com.booknest.pricing.Pricing;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

/**
 * The BookNest console shop: menu, input, and output.
 */
public class BookNestShop {

    private static final Catalog CATALOG = new Catalog(
            new Book("The Quiet Library", "12.99"),
            new Book("Java for Everyone", "29.50"),
            new Book("Bookmark set", "3.35"),
            new Book("Reading lamp", "24.00"));

    private static final String VIEW_CART = "9";
    private static final String CHECKOUT = "0";
    private static final NumberFormat MONEY =
            NumberFormat.getCurrencyInstance(Locale.US);
    private static final String SUMMARY_LINE = "%-22s %10s%n";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cart cart = new Cart(CATALOG);

        System.out.println("Welcome to BookNest!");
        String choice = askForChoice(scanner);

        while (!choice.equals(CHECKOUT)) {
            int product = toProductIndex(choice);
            if (choice.equals(VIEW_CART)) {
                printCart(cart);
            } else if (product >= 0) {
                int quantity = readQuantity(scanner);
                cart.add(product, quantity);
                System.out.println("Added " + quantity + " x "
                        + CATALOG.get(product).title() + ".");
            } else {
                System.out.println("Unknown option: " + choice);
            }
            choice = askForChoice(scanner);
        }

        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. See you soon!");
            return;
        }

        MembershipLevel level = readMembership(scanner);
        printCart(cart);
        printSummary(cart, level);
    }

    // ---- Menu -----------------------------------------------------

    private static String askForChoice(Scanner scanner) {
        printMenu();
        System.out.print("Choose an option: ");
        return scanner.nextLine().strip();
    }

    private static void printMenu() {
        System.out.println();
        for (int i = 0; i < CATALOG.size(); i++) {
            Book book = CATALOG.get(i);
            System.out.printf("%d) %-20s %8s%n",
                    i + 1, book.title(), money(book.price()));
        }
        System.out.println(VIEW_CART + ") View cart");
        System.out.println(CHECKOUT + ") Checkout");
    }

    /** Returns the catalog index for a menu choice, or -1. */
    private static int toProductIndex(String choice) {
        if (!choice.matches("\\d")) {
            return -1;
        }
        int number = Integer.parseInt(choice);
        if (number < 1 || number > CATALOG.size()) {
            return -1;
        }
        return number - 1;
    }

    // ---- Input ----------------------------------------------------

    private static int readQuantity(Scanner scanner) {
        while (true) {
            System.out.print("Quantity (1-10): ");
            String text = scanner.nextLine().strip();
            if (text.matches("\\d{1,2}")) {
                int quantity = Integer.parseInt(text);
                if (quantity >= 1 && quantity <= 10) {
                    return quantity;
                }
            }
            System.out.println(
                    "Please enter a whole number from 1 to 10.");
        }
    }

    private static MembershipLevel readMembership(Scanner scanner) {
        while (true) {
            System.out.print(
                    "Membership (none/bronze/silver/gold): ");
            String text = scanner.nextLine().strip();
            for (MembershipLevel level : MembershipLevel.values()) {
                if (level.name().equalsIgnoreCase(text)) {
                    return level;
                }
            }
            System.out.println("Unknown membership: " + text);
        }
    }

    // ---- Output ---------------------------------------------------

    private static void printCart(Cart cart) {
        System.out.println();
        System.out.println("Your cart:");
        if (cart.isEmpty()) {
            System.out.println("  (empty)");
            return;
        }
        for (CartLine line : cart.lines()) {
            System.out.printf("  %2d x %-20s %9s%n", line.quantity(),
                    line.book().title(), money(line.total()));
        }
    }

    private static void printSummary(Cart cart,
                                     MembershipLevel level) {
        BigDecimal subtotal = cart.subtotal();
        BigDecimal discount = Pricing.memberDiscount(subtotal, level);
        BigDecimal taxable = subtotal.subtract(discount);
        BigDecimal tax = Pricing.tax(taxable);
        BigDecimal shipping = Pricing.shipping(cart.totalItems());
        BigDecimal total = taxable.add(tax).add(shipping);

        System.out.println();
        printSummaryLine("Subtotal", money(subtotal));
        printSummaryLine("Discount (" + level + " "
                + level.discountPercent() + "%)",
                money(discount.negate()));
        printSummaryLine("Tax (8.25%)", money(tax));
        printSummaryLine("Shipping",
                shipping.signum() == 0 ? "FREE" : money(shipping));
        printSummaryLine("TOTAL", money(total));
        System.out.println("Thank you for shopping at BookNest!");
    }

    private static void printSummaryLine(String label, String value) {
        System.out.printf(SUMMARY_LINE, label, value);
    }

    private static String money(BigDecimal amount) {
        return MONEY.format(amount);
    }
}
