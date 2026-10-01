package com.booknest.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.booknest.cart.CartLine;
import com.booknest.catalog.Accessory;
import com.booknest.catalog.Book;
import com.booknest.pricing.MembershipLevel;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class DailyReportTest {

    private static final ZoneId CHICAGO =
            ZoneId.of("America/Chicago");
    private static final LocalDate FRIDAY = LocalDate.of(2026, 3, 6);

    private static Order order(String id, MembershipLevel level,
                               int books, int bookmarks, String total,
                               String placedAtUtc) {
        return new Order(id, level, List.of(
                new CartLine(
                        new Book("Dune", "Frank Herbert", "9.99"),
                        books),
                new CartLine(new Accessory("Bookmark set", "3.35"),
                        bookmarks)),
                new BigDecimal(total), Instant.parse(placedAtUtc));
    }

    private final List<Order> orders = List.of(
            // 11:15 AM on Friday in Chicago
            order("BN-1001", MembershipLevel.GOLD, 1, 2, "20.00",
                    "2026-03-06T17:15:00Z"),
            // 10:00 PM on Friday in Chicago: already Saturday in UTC
            order("BN-1002", MembershipLevel.NONE, 2, 1, "30.00",
                    "2026-03-07T04:00:00Z"),
            // Saturday morning in Chicago
            order("BN-1003", MembershipLevel.NONE, 5, 5, "99.00",
                    "2026-03-07T15:00:00Z"));

    @Test
    void countsOnlyTheDaysOrdersInTheStoresTimeZone() {
        DailyReport report =
                DailyReport.of(orders.stream(), FRIDAY, CHICAGO);

        assertEquals(List.of("BN-1001", "BN-1002"),
                report.orders().stream().map(Order::id).toList());
        assertEquals(1, report.memberOrders());
        assertEquals(new BigDecimal("50.00"), report.revenue());
        assertEquals(Map.of("Bookmark set", 3, "Dune", 3),
                report.itemsSold());
    }

    @Test
    void aDayWithoutOrdersIsAllZeros() {
        DailyReport report = DailyReport.of(Stream.empty(), FRIDAY,
                CHICAGO);

        assertEquals(0, report.orders().size());
        assertEquals(BigDecimal.ZERO, report.revenue());
        assertEquals(Map.of(), report.itemsSold());
    }
}
