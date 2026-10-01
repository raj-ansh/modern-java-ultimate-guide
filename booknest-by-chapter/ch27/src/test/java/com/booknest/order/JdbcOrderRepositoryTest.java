package com.booknest.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.booknest.cart.CartLine;
import com.booknest.catalog.Accessory;
import com.booknest.catalog.EBook;
import com.booknest.common.Database;
import com.booknest.pricing.MembershipLevel;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** An integration test: a real (in-memory) database, real SQL. */
class JdbcOrderRepositoryTest {

    private JdbcOrderRepository repository;

    @BeforeEach
    void newEmptyDatabase() {
        // A unique name gives every test its own, empty database
        String url = "jdbc:h2:mem:" + UUID.randomUUID()
                + ";DB_CLOSE_DELAY=-1";
        repository = new JdbcOrderRepository(new Database(url));
    }

    /** An order placed at 11:15 AM Chicago time on a March day. */
    private static Order order(String id, String total, int day) {
        return new Order(id, MembershipLevel.SILVER,
                List.of(new CartLine(new EBook("Java for Everyone",
                                "Dev Malhotra", "19.99"), 1),
                        new CartLine(new Accessory("Bookmark set",
                                "3.35"), 2)),
                new BigDecimal(total), Instant.parse(
                        "2026-03-%02dT17:15:00Z".formatted(day)));
    }

    @Test
    void aSavedOrderComesBackEqual() {
        Order saved = order("BN-1001", "29.54", 6);
        repository.save(saved);

        assertEquals(Optional.of(saved),
                repository.findById("BN-1001"));
    }

    @Test
    void anUnknownIdIsEmpty() {
        assertTrue(repository.findById("BN-4040").isEmpty());
    }

    @Test
    void savingTheSameIdReplacesTheOrder() {
        repository.save(order("BN-1001", "29.54", 6));
        Order changed = order("BN-1001", "25.00", 6);
        repository.save(changed);

        assertEquals(1, repository.count());
        assertEquals(changed, repository.getById("BN-1001"));
    }

    @Test
    void streamReturnsOrdersOldestFirst() {
        repository.save(order("BN-1002", "10.00", 7));
        repository.save(order("BN-1001", "10.00", 6));

        assertEquals(List.of("BN-1001", "BN-1002"),
                repository.stream().map(Order::id).toList());
    }

    @Test
    void deleteAllEmptiesTheRepository() {
        repository.save(order("BN-1001", "10.00", 6));
        repository.deleteAll();

        assertEquals(0, repository.count());
    }
}
