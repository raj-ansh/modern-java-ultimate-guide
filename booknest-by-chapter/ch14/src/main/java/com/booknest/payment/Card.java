package com.booknest.payment;

import java.util.Objects;

/**
 * Payment by card. Only the last four digits are kept: storing full
 * card numbers requires special security certification.
 *
 * @param lastFourDigits exactly four digits
 */
public record Card(String lastFourDigits) implements PaymentMethod {

    public Card {
        Objects.requireNonNull(lastFourDigits);
        if (!lastFourDigits.matches("\\d{4}")) {
            throw new IllegalArgumentException(
                    "need exactly 4 digits: " + lastFourDigits);
        }
    }
}
