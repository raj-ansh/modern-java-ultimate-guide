package com.booknest;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class BookNestShop {

    static final NumberFormat MONEY =
            NumberFormat.getCurrencyInstance(Locale.US);
    static final String SUMMARY_LINE = "%-16s %10s%n";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BigDecimal subtotal = BigDecimal.ZERO;
        int itemsInCart = 0;

        System.out.println("Welcome to BookNest!");
        String choice = askForChoice(scanner);

        while (!choice.equals("0")) {
            if (isProduct(choice)) {
                int quantity = readQuantity(scanner);
                BigDecimal lineTotal =
                        Pricing.lineTotal(priceOf(choice), quantity);
                subtotal = subtotal.add(lineTotal);
                itemsInCart += quantity;
                printAdded(quantity, titleOf(choice),
                        itemsInCart, subtotal);
            } else {
                System.out.println("Unknown option: " + choice);
            }
            choice = askForChoice(scanner);
        }

        if (itemsInCart == 0) {
            System.out.println("Your cart is empty. See you soon!");
            return;
        }

        boolean isMember = askYesNo(scanner,
                "Are you a BookNest member? (yes/no): ");
        printSummary(subtotal, itemsInCart, isMember);
    }

    // ---- Menu and catalog ----------------------------------------

    static String askForChoice(Scanner scanner) {
        printMenu();
        System.out.print("Choose an option: ");
        return scanner.nextLine().strip();
    }

    static void printMenu() {
        System.out.println();
        System.out.println("1) The Quiet Library    $12.99");
        System.out.println("2) Java for Everyone    $29.50");
        System.out.println("3) Bookmark set          $3.35");
        System.out.println("0) Checkout");
    }

    static boolean isProduct(String choice) {
        return switch (choice) {
            case "1", "2", "3" -> true;
            default -> false;
        };
    }

    static String titleOf(String choice) {
        return switch (choice) {
            case "1" -> "The Quiet Library";
            case "2" -> "Java for Everyone";
            default -> "Bookmark set";
        };
    }

    static BigDecimal priceOf(String choice) {
        return switch (choice) {
            case "1" -> new BigDecimal("12.99");
            case "2" -> new BigDecimal("29.50");
            default -> new BigDecimal("3.35");
        };
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

    static void printAdded(int quantity, String title,
                           int itemsInCart, BigDecimal subtotal) {
        System.out.println("Added " + quantity + " x " + title
                + ". Cart: " + itemsInCart + " item(s), "
                + money(subtotal));
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
