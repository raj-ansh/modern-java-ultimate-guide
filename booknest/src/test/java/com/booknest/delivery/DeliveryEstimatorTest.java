package com.booknest.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DeliveryEstimatorTest {

    private final DeliveryEstimator standard =
            DeliveryEstimator.standard();

    @ParameterizedTest(name = "placed {0}: sent {1}, arrives {2}")
    @CsvSource({
            // placed          dispatched  delivered
            "2026-03-06T11:15, 2026-03-06, 2026-03-11", // Fri 11:15
            "2026-03-06T16:30, 2026-03-09, 2026-03-12", // Fri 16:30
            "2026-03-07T10:00, 2026-03-09, 2026-03-12", // Saturday
            "2026-03-09T13:59, 2026-03-09, 2026-03-12", // Mon 13:59
            "2026-03-09T14:00, 2026-03-10, 2026-03-13"  // Mon 14:00
    })
    void dispatchAndDeliveryDates(LocalDateTime placed,
                                  LocalDate dispatched,
                                  LocalDate delivered) {
        assertEquals(dispatched, standard.dispatchDate(placed));
        assertEquals(delivered, standard.deliveryDate(placed));
    }

    @Test
    void transitMustTakeAtLeastOneDay() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new DeliveryEstimator(LocalTime.NOON, 0));
        assertEquals("transit takes at least a day: 0",
                e.getMessage());
    }
}
