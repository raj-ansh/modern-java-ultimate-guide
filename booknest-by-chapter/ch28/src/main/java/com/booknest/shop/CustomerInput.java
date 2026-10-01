package com.booknest.shop;

import com.booknest.payment.Card;
import com.booknest.payment.CashOnDelivery;
import com.booknest.payment.PaymentMethod;
import com.booknest.pricing.MembershipLevel;
import com.booknest.pricing.Promotion;
import com.booknest.pricing.Promotions;
import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;

/**
 * Asks the customer questions and insists on valid answers. Each
 * method keeps asking until the answer makes sense.
 */
final class CustomerInput {

    private final Scanner scanner;
    private final PrintStream out;

    CustomerInput(Scanner scanner, PrintStream out) {
        this.scanner = scanner;
        this.out = out;
    }

    /** Prints the prompt, and returns the answer without spaces. */
    String ask(String prompt) {
        out.print(prompt);
        return scanner.nextLine().strip();
    }

    int readQuantity() {
        while (true) {
            String text = ask("Quantity (1-10): ");
            try {
                int quantity = Integer.parseInt(text);
                if (quantity >= 1 && quantity <= 10) {
                    return quantity;
                }
            } catch (NumberFormatException _) {
                // not a whole number: fall through to the message
            }
            out.println("Please enter a whole number from 1 to 10.");
        }
    }

    MembershipLevel readMembership() {
        while (true) {
            String text =
                    ask("Membership (none/bronze/silver/gold): ");
            Optional<MembershipLevel> level =
                    MembershipLevel.parse(text);
            if (level.isPresent()) {
                return level.orElseThrow();
            }
            out.println("Unknown membership: " + text);
        }
    }

    Promotion readPromotion() {
        while (true) {
            String text = ask("Promo code (or none): ");
            Optional<Promotion> found = Promotions.find(text);
            if (found.isPresent()) {
                return found.orElseThrow();
            }
            out.println("Unknown promo code: " + text);
        }
    }

    PaymentMethod readPayment(boolean canPayOnDelivery) {
        while (true) {
            String text = ask("Payment (card/cash): ");
            if (text.equalsIgnoreCase("card")) {
                return new Card(readLastFourDigits());
            }
            if (text.equalsIgnoreCase("cash") && canPayOnDelivery) {
                return new CashOnDelivery();
            }
            if (text.equalsIgnoreCase("cash")) {
                out.println("Nothing will be delivered, "
                        + "so please pay by card.");
            } else {
                out.println("Unknown payment method: " + text);
            }
        }
    }

    private String readLastFourDigits() {
        while (true) {
            String digits = ask("Last four card digits: ");
            if (digits.matches("\\d{4}")) {
                return digits;
            }
            out.println("Please enter exactly 4 digits.");
        }
    }
}
