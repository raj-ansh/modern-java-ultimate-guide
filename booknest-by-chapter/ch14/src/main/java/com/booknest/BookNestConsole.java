package com.booknest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class BookNestConsole {

    static final BigDecimal MEMBER_DISCOUNT = new BigDecimal("0.10");
    static final BigDecimal TAX_RATE = new BigDecimal("0.0825");
    static final BigDecimal SHIPPING_FEE = new BigDecimal("4.99");
    static final int FREE_SHIPPING_ITEMS = 5;
    static final NumberFormat MONEY =
            NumberFormat.getCurrencyInstance(Locale.US);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BigDecimal subtotal = BigDecimal.ZERO;
        int itemsInCart = 0;
        boolean shopping = true;

        System.out.println("Welcome to BookNest!");

        while (shopping) {
            System.out.println();
            System.out.println("1) The Quiet Library    $12.99");
            System.out.println("2) Java for Everyone    $29.50");
            System.out.println("3) Bookmark set          $3.35");
            System.out.println("0) Checkout");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().strip();

            switch (choice) {
                case "1", "2", "3" -> {
                    String title = switch (choice) {
                        case "1" -> "The Quiet Library";
                        case "2" -> "Java for Everyone";
                        default -> "Bookmark set";
                    };
                    BigDecimal price = switch (choice) {
                        case "1" -> new BigDecimal("12.99");
                        case "2" -> new BigDecimal("29.50");
                        default -> new BigDecimal("3.35");
                    };

                    int quantity = readQuantity(scanner);
                    BigDecimal lineTotal = price.multiply(
                            BigDecimal.valueOf(quantity));
                    subtotal = subtotal.add(lineTotal);
                    itemsInCart += quantity;

                    System.out.println("Added " + quantity + " x "
                            + title + ". Cart: " + itemsInCart
                            + " item(s), " + MONEY.format(subtotal));
                }
                case "0" -> shopping = false;
                default -> System.out.println("Unknown option: "
                        + choice);
            }
        }

        if (itemsInCart == 0) {
            System.out.println("Your cart is empty. See you soon!");
            return;
        }

        System.out.print("Are you a BookNest member? (yes/no): ");
        String answer = scanner.nextLine().strip();
        boolean isMember = answer.equalsIgnoreCase("yes");

        BigDecimal discount = BigDecimal.ZERO;
        if (isMember) {
            discount = subtotal.multiply(MEMBER_DISCOUNT)
                    .setScale(2, RoundingMode.HALF_UP);
        }
        BigDecimal taxable = subtotal.subtract(discount);
        BigDecimal tax = taxable.multiply(TAX_RATE)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal shipping = itemsInCart >= FREE_SHIPPING_ITEMS
                ? BigDecimal.ZERO
                : SHIPPING_FEE;
        BigDecimal total = taxable.add(tax).add(shipping);

        String line = "%-16s %10s%n";
        System.out.println();
        System.out.printf(line, "Subtotal", MONEY.format(subtotal));
        System.out.printf(line, "Member discount",
                MONEY.format(discount.negate()));
        System.out.printf(line, "Tax (8.25%)", MONEY.format(tax));
        String shippingText = shipping.signum() == 0
                ? "FREE"
                : MONEY.format(shipping);
        System.out.printf(line, "Shipping", shippingText);
        System.out.printf(line, "TOTAL", MONEY.format(total));
        System.out.println("Thank you for shopping at BookNest!");
    }

    static int readQuantity(Scanner scanner) {
        int quantity = 0;
        while (quantity < 1 || quantity > 10) {
            System.out.print("Quantity (1-10): ");
            String text = scanner.nextLine().strip();

            if (text.matches("\\d{1,2}")) {
                quantity = Integer.parseInt(text);
            }
            if (quantity < 1 || quantity > 10) {
                System.out.println(
                        "Please enter a whole number from 1 to 10.");
            }
        }
        return quantity;
    }
}
