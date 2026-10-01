package com.booknest.delivery;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Estimates when a shipped order arrives. Orders placed on a business
 * day before the cutoff leave the warehouse that day; all others
 * leave on the next business day. Delivery then takes a fixed number
 * of business days. Business days are Monday to Friday.
 *
 * @param cutoff      the latest time for same-day dispatch
 * @param transitDays business days from dispatch to delivery
 */
public record DeliveryEstimator(LocalTime cutoff, int transitDays) {

    public DeliveryEstimator {
        Objects.requireNonNull(cutoff, "cutoff must not be null");
        if (transitDays < 1) {
            throw new IllegalArgumentException(
                    "transit takes at least a day: " + transitDays);
        }
    }

    /** Dispatch by 2 PM, delivery in 3 business days. */
    public static DeliveryEstimator standard() {
        return new DeliveryEstimator(LocalTime.of(14, 0), 3);
    }

    /** Returns the day the order leaves the warehouse. */
    public LocalDate dispatchDate(LocalDateTime placedAt) {
        LocalDate day = placedAt.toLocalDate();
        boolean sameDay = isBusinessDay(day)
                && placedAt.toLocalTime().isBefore(cutoff);
        return sameDay ? day : businessDaysAfter(day, 1);
    }

    /** Returns the day the order should arrive. */
    public LocalDate deliveryDate(LocalDateTime placedAt) {
        return businessDaysAfter(dispatchDate(placedAt), transitDays);
    }

    static boolean isBusinessDay(LocalDate day) {
        DayOfWeek weekday = day.getDayOfWeek();
        return weekday != DayOfWeek.SATURDAY
                && weekday != DayOfWeek.SUNDAY;
    }

    /** Returns the count-th business day after the given day. */
    static LocalDate businessDaysAfter(LocalDate day, int count) {
        return Stream.iterate(day.plusDays(1), d -> d.plusDays(1))
                .filter(DeliveryEstimator::isBusinessDay)
                .skip(count - 1)
                .findFirst()
                .orElseThrow();   // an endless stream always has one
    }
}
