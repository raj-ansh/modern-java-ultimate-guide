package com.booknest;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class BookNestShop {

    // ---- The catalog: the ONLY place products are defined --------

    static final String[] TITLES = {
            "The Quiet Library",
            "Java for Everyone",
            "Bookmark set",
            "Reading lamp"
    };
    static final BigDecimal[] PRICES = {
            new BigDecimal("12.99"),
            new BigDecimal("29.50"),
            new BigDecimal("3.35"),
            new BigDecimal("24.00")
    };

    static final String VIEW_CART = "9";
    static final String CHECKOUT = "0";
    static final NumberFormat MONEY =
            NumberFormat.getCurrencyInstance(Locale.US);
    static final String SUMMARY_LINE = "%-16s %10s%n";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] quantities = new int[TITLES.length];

        System.out.println("Welcome to BookNest!");
        String choice = askForChoice(scanner);

        while (!choice.equals(CHECKOUT)) {
            int product = toProductIndex(choice);
            if (choice.equals(VIEW_CART)) {
                printCart(quantities);
            } else if (product >= 0) {
                int quantity = readQuantity(scanner);
                quantities[product] += quantity;
                System.out.println("Added " + quantity + " x "
                        + TITLES[product] + ".");
            } else {
                System.out.println("Unknown option: " + choice);
            }
            choice = askForChoice(scanner);
        }

        int itemsInCart = totalItems(quantities);
        if (itemsInCart == 0) {
            System.out.println("Your cart is empty. See you soon!");
            return;
        }

        boolean isMember = askYesNo(scanner,
                "Are you a BookNest member? (yes/no): ");
        printCart(quantities);
        printSummary(subtotal(quantities), itemsInCart, isMember);
    }

    // ---- Menu and catalog ----------------------------------------

    static String askForChoice(Scanner scanner) {
        printMenu();
        System.out.print("Choose an option: ");
        return scanner.nextLine().strip();
    }

    static void printMenu() {
        System.out.println();
        for (int i = 0; i < TITLES.length; i++) {
            System.out.printf("%d) %-20s %8s%n",
                    i + 1, TITLES[i], money(PRICES[i]));
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
        if (number < 1 || number > TITLES.length) {
            return -1;
        }
        return number - 1;
    }

    // ---- Cart calculations ---------------------------------------

    static int totalItems(int[] quantities) {
        int total = 0;
        for (int quantity : quantities) {
            total += quantity;
        }
        return total;
    }

    static BigDecimal subtotal(int[] quantities) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (int i = 0; i < quantities.length; i++) {
            subtotal = subtotal.add(
                    Pricing.lineTotal(PRICES[i], quantities[i]));
        }
        return subtotal;
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

    static void printCart(int[] quantities) {
        System.out.println();
        System.out.println("Your cart:");
        if (totalItems(quantities) == 0) {
            System.out.println("  (empty)");
            return;
        }
        for (int i = 0; i < quantities.length; i++) {
            if (quantities[i] > 0) {
                BigDecimal lineTotal =
                        Pricing.lineTotal(PRICES[i], quantities[i]);
                System.out.printf("  %2d x %-20s %9s%n",
                        quantities[i], TITLES[i], money(lineTotal));
            }
        }
    }

    static void printSummary(BigDecimal subtotal, int itemsInCart,
                             boolean isMember) {
        BigDecimal discount =
                Pricing.memberDiscount(subtotal, isMember);
        BigDecimal taxable = subtotal.subtract(discount);
        BigDecimal tax = Pricing.tax(taxable);
        BigDecimal shipping = Pricing.shipping(itemsInCart);
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
