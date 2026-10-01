package com.booknest;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class BookNestShop {

    static final Book[] CATALOG = {
            new Book("The Quiet Library", "12.99"),
            new Book("Java for Everyone", "29.50"),
            new Book("Bookmark set", "3.35"),
            new Book("Reading lamp", "24.00")
    };

    static final String VIEW_CART = "9";
    static final String CHECKOUT = "0";
    static final NumberFormat MONEY =
            NumberFormat.getCurrencyInstance(Locale.US);
    static final String SUMMARY_LINE = "%-16s %10s%n";

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
                        + CATALOG[product].getTitle() + ".");
            } else {
                System.out.println("Unknown option: " + choice);
            }
            choice = askForChoice(scanner);
        }

        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. See you soon!");
            return;
        }

        boolean isMember = askYesNo(scanner,
                "Are you a BookNest member? (yes/no): ");
        printCart(cart);
        printSummary(cart, isMember);
    }

    // ---- Menu -----------------------------------------------------

    static String askForChoice(Scanner scanner) {
        printMenu();
        System.out.print("Choose an option: ");
        return scanner.nextLine().strip();
    }

    static void printMenu() {
        System.out.println();
        for (int i = 0; i < CATALOG.length; i++) {
            Book book = CATALOG[i];
            System.out.printf("%d) %-20s %8s%n",
                    i + 1, book.getTitle(), money(book.getPrice()));
        }
        System.out.println(VIEW_CART + ") View cart");
        System.out.println(CHECKOUT + ") Checkout");
    }

    /** Returns the catalog index for a menu choice, or -1. */
    static int toProductIndex(String choice) {
        if (!choice.matches("\\d")) {
            return -1;
        }
        int number = Integer.parseInt(choice);
        if (number < 1 || number > CATALOG.length) {
            return -1;
        }
        return number - 1;
    }

    // ---- Input ----------------------------------------------------

    static int readQuantity(Scanner scanner) {
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

    static boolean askYesNo(Scanner scanner, String question) {
        System.out.print(question);
        return scanner.nextLine().strip().equalsIgnoreCase("yes");
    }

    // ---- Output ---------------------------------------------------

    static void printCart(Cart cart) {
        System.out.println();
        System.out.println("Your cart:");
        if (cart.isEmpty()) {
            System.out.println("  (empty)");
            return;
        }
        for (int i = 0; i < CATALOG.length; i++) {
            int quantity = cart.quantityOf(i);
            if (quantity > 0) {
                Book book = CATALOG[i];
                System.out.printf("  %2d x %-20s %9s%n", quantity,
                        book.getTitle(),
                        money(book.priceFor(quantity)));
            }
        }
    }

    static void printSummary(Cart cart, boolean isMember) {
        BigDecimal subtotal = cart.subtotal();
        BigDecimal discount =
                Pricing.memberDiscount(subtotal, isMember);
        BigDecimal taxable = subtotal.subtract(discount);
        BigDecimal tax = Pricing.tax(taxable);
        BigDecimal shipping = Pricing.shipping(cart.totalItems());
        BigDecimal total = taxable.add(tax).add(shipping);

        System.out.println();
        printSummaryLine("Subtotal", money(subtotal));
        printSummaryLine("Member discount", money(discount.negate()));
        printSummaryLine("Tax (8.25%)", money(tax));
        printSummaryLine("Shipping",
                shipping.signum() == 0 ? "FREE" : money(shipping));
        printSummaryLine("TOTAL", money(total));
        System.out.println("Thank you for shopping at BookNest!");
    }

    static void printSummaryLine(String label, String value) {
        System.out.printf(SUMMARY_LINE, label, value);
    }

    static String money(BigDecimal amount) {
        return MONEY.format(amount);
    }
}
