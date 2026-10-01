package com.booknest.shop;

import com.booknest.cart.CartLine;
import com.booknest.catalog.Book;
import com.booknest.common.InMemoryRepository;
import com.booknest.common.Repository;
import com.booknest.order.Order;
import com.booknest.pricing.MembershipLevel;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Simulates 100 trading days in one long-running JVM, the way a
 * server runs for months. With the argument "keep", the repository
 * never forgets an order. Without it, each day's orders are archived
 * (they're already in the order log) and cleared from memory.
 */
public final class LongRun {

    private static final int DAYS = 100;
    private static final int ORDERS_PER_DAY = 2_000;

    public static void main(String[] args) {
        boolean keepEverything =
                args.length > 0 && args[0].equals("keep");
        Repository<Order, String> orders = new InMemoryRepository<>();
        Book book =
                new Book("The Quiet Library", "Asha Rao", "12.99");
        List<CartLine> lines = List.of(new CartLine(book, 1));
        Instant firstDay = Instant.parse("2026-01-02T15:00:00Z");

        int day = 0;
        long placed = 0;
        int mostInMemory = 0;
        try {
            for (day = 1; day <= DAYS; day++) {
                Instant today = firstDay.plus(day, ChronoUnit.DAYS);
                for (int n = 1; n <= ORDERS_PER_DAY; n++) {
                    orders.save(new Order("D" + day + "-" + n,
                            MembershipLevel.NONE, lines,
                            new BigDecimal("19.05"), today));
                    placed++;
                }
                mostInMemory = Math.max(mostInMemory, orders.count());
                if (!keepEverything) {
                    orders.deleteAll();     // end of day: let go
                }
            }
            System.out.println("Days traded:        " + DAYS);
            System.out.println("Orders placed:      " + placed);
            System.out.println("Most kept at once:  " + mostInMemory);
        } catch (OutOfMemoryError e) {
            orders = null;                  // free memory to report
            System.out.println("OutOfMemoryError: " + e.getMessage());
            System.out.println("Stopped before day " + DAYS + "? "
                    + (day < DAYS));
        }
    }
}
