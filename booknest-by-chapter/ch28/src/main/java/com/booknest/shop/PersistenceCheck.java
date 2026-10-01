package com.booknest.shop;

import com.booknest.cart.CartLine;
import com.booknest.catalog.Accessory;
import com.booknest.catalog.Book;
import com.booknest.catalog.EBook;
import com.booknest.common.Database;
import com.booknest.common.Repository;
import com.booknest.order.JdbcOrderRepository;
import com.booknest.order.Order;
import com.booknest.pricing.MembershipLevel;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

/**
 * Proves that orders survive a restart: saves two orders to a
 * database file, then reads them back through a brand-new repository.
 */
public final class PersistenceCheck {

    public static void main(String[] args) throws IOException {
        Path data = Path.of("data");
        Files.deleteIfExists(data.resolve("check.mv.db"));  // fresh
        Database database = new Database(
                "jdbc:h2:" + data.resolve("check").toAbsolutePath());

        Order first = new Order("BN-1001", MembershipLevel.GOLD,
                List.of(new CartLine(new Book("The Quiet Library",
                                "Asha Rao", "12.99"), 2),
                        new CartLine(new Accessory("Bookmark set",
                                "3.35"), 1)),
                new BigDecimal("31.98"),
                Instant.parse("2026-03-06T17:15:00Z"));
        Order second = new Order("BN-1002", MembershipLevel.NONE,
                List.of(new CartLine(new EBook("Java for Everyone",
                        "Dev Malhotra", "19.99"), 1)),
                new BigDecimal("21.64"),
                Instant.parse("2026-03-06T22:30:00Z"));

        Repository<Order, String> before =
                new JdbcOrderRepository(database);
        before.save(first);
        before.save(second);
        System.out.println("Saved: " + before.count() + " orders");

        // A new repository: nothing shared in memory, only the file
        Repository<Order, String> after =
                new JdbcOrderRepository(database);
        System.out.println("After restart: " + after.count()
                + " orders");
        after.forEach(order -> System.out.println("  " + order.id()
                + "  " + order.level() + "  " + order.items()
                + (order.items() == 1 ? " item   $" : " items  $")
                + order.total() + "  "
                + order.placedAt()));

        Order reloaded = after.getById("BN-1001");
        for (CartLine line : reloaded.lines()) {
            System.out.println("    " + line.quantity() + " x "
                    + line.product().menuLabel() + " at $"
                    + line.product().getPrice());
        }
        System.out.println("Identical to what was saved? "
                + (reloaded.equals(first)
                        && after.getById("BN-1002").equals(second)));
        System.out.println("BN-9999 found? "
                + after.findById("BN-9999").isPresent());
    }
}
