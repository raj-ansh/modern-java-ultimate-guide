package com.booknest;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class OrderCalculator {

    static final BigDecimal MEMBER_DISCOUNT = new BigDecimal("0.10");
    static final BigDecimal TAX_RATE = new BigDecimal("0.0825");

    public static void main(String[] args) {
        // Cart: 2 novels, 1 notebook, 3 bookmarks
        BigDecimal novels = new BigDecimal("12.99")
                .multiply(BigDecimal.valueOf(2));
        BigDecimal notebook = new BigDecimal("7.49");
        BigDecimal bookmarks = new BigDecimal("3.35")
                .multiply(BigDecimal.valueOf(3));
        boolean isMember = true;

        BigDecimal subtotal = novels.add(notebook).add(bookmarks);

        BigDecimal discount = BigDecimal.ZERO;
        if (isMember) {
            discount = subtotal.multiply(MEMBER_DISCOUNT)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal taxable = subtotal.subtract(discount);
        BigDecimal tax = taxable.multiply(TAX_RATE)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = taxable.add(tax);

        System.out.println("Subtotal:        " + subtotal);
        System.out.println("Member discount: -" + discount);
        System.out.println("Tax (8.25%):     " + tax);
        System.out.println("Total:           " + total);
    }
}
