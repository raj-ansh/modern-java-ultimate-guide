package com.booknest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class ReceiptPrinter {

    static final BigDecimal MEMBER_DISCOUNT = new BigDecimal("0.10");
    static final BigDecimal TAX_RATE = new BigDecimal("0.0825");
    static final NumberFormat MONEY =
            NumberFormat.getCurrencyInstance(Locale.US);

    static final int WIDTH = 43;
    static final String ITEM_FORMAT = "%-18s %3s %9s %10s%n";
    static final String TOTAL_FORMAT = "%-29s %13s%n";

    public static void main(String[] args) {
        // Raw input, as it might arrive from a web form
        String rawCustomer = "   Priya Sharma  ";
        String rawOrderId = "bn-1042";

        String customer = rawCustomer.strip();
        String orderId = rawOrderId.toUpperCase();

        StringBuilder receipt = new StringBuilder();
        receipt.append("""
                %s
                %s
                %s
                Order:    %s
                Customer: %s
                %s
                """.formatted(
                        "=".repeat(WIDTH),
                        center("BOOKNEST RECEIPT"),
                        "=".repeat(WIDTH),
                        orderId,
                        customer,
                        "-".repeat(WIDTH)));

        receipt.append(ITEM_FORMAT.formatted(
                "Item", "Qty", "Price", "Total"));

        BigDecimal novels = addItem(receipt, "Novel", 2, "12.99");
        BigDecimal notebook = addItem(receipt, "Notebook", 1, "7.49");
        BigDecimal bookmark = addItem(receipt, "Bookmark", 3, "3.35");
        BigDecimal subtotal = novels.add(notebook).add(bookmark);

        BigDecimal discount = subtotal.multiply(MEMBER_DISCOUNT)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal taxable = subtotal.subtract(discount);
        BigDecimal tax = taxable.multiply(TAX_RATE)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = taxable.add(tax);

        receipt.append("-".repeat(WIDTH)).append('\n');
        receipt.append(TOTAL_FORMAT.formatted(
                "Subtotal", MONEY.format(subtotal)));
        receipt.append(TOTAL_FORMAT.formatted(
                "Member discount (10%)",
                MONEY.format(discount.negate())));
        receipt.append(TOTAL_FORMAT.formatted(
                "Tax (8.25%)", MONEY.format(tax)));
        receipt.append(TOTAL_FORMAT.formatted(
                "TOTAL", MONEY.format(total)));
        receipt.append("=".repeat(WIDTH)).append('\n');

        System.out.print(receipt);
    }

    static BigDecimal addItem(StringBuilder receipt, String name,
                              int quantity, String unitPrice) {
        BigDecimal price = new BigDecimal(unitPrice);
        BigDecimal lineTotal =
                price.multiply(BigDecimal.valueOf(quantity));
        receipt.append(ITEM_FORMAT.formatted(
                name, quantity, MONEY.format(price),
                MONEY.format(lineTotal)));
        return lineTotal;
    }

    static String center(String text) {
        int padding = (WIDTH - text.length()) / 2;
        return " ".repeat(padding) + text;
    }
}
