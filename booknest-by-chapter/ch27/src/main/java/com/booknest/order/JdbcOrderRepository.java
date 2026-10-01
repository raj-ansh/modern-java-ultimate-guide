package com.booknest.order;

import com.booknest.cart.CartLine;
import com.booknest.catalog.Accessory;
import com.booknest.catalog.Book;
import com.booknest.catalog.EBook;
import com.booknest.catalog.Product;
import com.booknest.common.DataAccessException;
import com.booknest.common.Database;
import com.booknest.common.Repository;
import com.booknest.pricing.MembershipLevel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Stores orders in a relational database, through JDBC. Every method
 * uses its own connection, so the repository is as thread-safe as
 * the database itself. Order lines keep a copy of each product's
 * details, because prices change but a past order must not.
 */
public final class JdbcOrderRepository
        implements Repository<Order, String> {

    /** A unit of database work that may throw SQLException. */
    @FunctionalInterface
    private interface Work<R> {
        R apply(Connection db) throws SQLException;
    }

    private final Database database;

    public JdbcOrderRepository(Database database) {
        this.database = Objects.requireNonNull(database);
        createTablesIfMissing();
    }

    @Override
    public void save(Order order) {
        Objects.requireNonNull(order, "order must not be null");
        inTransaction(db -> {
            delete(db, order.id());         // replace, if it exists
            try (PreparedStatement insert = db.prepareStatement(
                    "INSERT INTO orders VALUES (?, ?, ?, ?)")) {
                insert.setString(1, order.id());
                insert.setString(2, order.level().name());
                insert.setBigDecimal(3, order.total());
                insert.setObject(4,
                        order.placedAt().atOffset(ZoneOffset.UTC));
                insert.executeUpdate();
            }
            String addLine =
                    "INSERT INTO order_line VALUES (?,?,?,?,?,?,?)";
            try (PreparedStatement insert =
                         db.prepareStatement(addLine)) {
                int lineNumber = 1;
                for (CartLine line : order.lines()) {
                    Product product = line.product();
                    insert.setString(1, order.id());
                    insert.setInt(2, lineNumber++);
                    insert.setString(3, kindOf(product));
                    insert.setString(4, product.getTitle());
                    insert.setString(5, product instanceof Book book
                            ? book.getAuthor() : null);
                    insert.setBigDecimal(6, product.getPrice());
                    insert.setInt(7, line.quantity());
                    insert.addBatch();
                }
                insert.executeBatch();
            }
            return null;
        });
    }

    @Override
    public Optional<Order> findById(String id) {
        return run(db -> {
            try (PreparedStatement find = db.prepareStatement(
                    "SELECT * FROM orders WHERE id = ?")) {
                find.setString(1, id);
                try (ResultSet rows = find.executeQuery()) {
                    if (!rows.next()) {
                        return Optional.empty();
                    }
                    Map<String, List<CartLine>> lines =
                            loadLines(db, id);
                    return Optional.of(toOrder(rows, lines));
                }
            }
        });
    }

    @Override
    public int count() {
        return run(db -> {
            try (Statement sql = db.createStatement();
                 ResultSet rows = sql.executeQuery(
                         "SELECT COUNT(*) FROM orders")) {
                rows.next();
                return rows.getInt(1);
            }
        });
    }

    @Override
    public void deleteAll() {
        inTransaction(db -> {
            try (Statement sql = db.createStatement()) {
                sql.executeUpdate("DELETE FROM order_line");
                sql.executeUpdate("DELETE FROM orders");
            }
            return null;
        });
    }

    /** All orders, oldest first. Two queries, however many orders. */
    @Override
    public Stream<Order> stream() {
        List<Order> orders = run(db -> {
            Map<String, List<CartLine>> lines = loadLines(db, null);
            List<Order> all = new ArrayList<>();
            String query =
                    "SELECT * FROM orders ORDER BY placed_at, id";
            try (Statement sql = db.createStatement();
                 ResultSet rows = sql.executeQuery(query)) {
                while (rows.next()) {
                    all.add(toOrder(rows, lines));
                }
            }
            return all;
        });
        return orders.stream();
    }

    // ---- Mapping between rows and objects -------------------------

    private static Order toOrder(ResultSet row,
                                 Map<String, List<CartLine>> lines)
            throws SQLException {
        String id = row.getString("id");
        return new Order(id,
                MembershipLevel.valueOf(row.getString("level")),
                lines.get(id),
                row.getBigDecimal("total"),
                row.getObject("placed_at", OffsetDateTime.class)
                        .toInstant());
    }

    /** Loads one order's lines, or every order's (id == null). */
    private static Map<String, List<CartLine>> loadLines(
            Connection db, String orderId) throws SQLException {
        String query = "SELECT * FROM order_line"
                + (orderId == null ? "" : " WHERE order_id = ?")
                + " ORDER BY order_id, line_no";
        Map<String, List<CartLine>> lines = new HashMap<>();
        try (PreparedStatement find = db.prepareStatement(query)) {
            if (orderId != null) {
                find.setString(1, orderId);
            }
            try (ResultSet rows = find.executeQuery()) {
                while (rows.next()) {
                    lines.computeIfAbsent(rows.getString("order_id"),
                                    _ -> new ArrayList<>())
                            .add(new CartLine(toProduct(rows),
                                    rows.getInt("quantity")));
                }
            }
        }
        return lines;
    }

    private static Product toProduct(ResultSet row)
            throws SQLException {
        String title = row.getString("title");
        String author = row.getString("author");
        String price =
                row.getBigDecimal("unit_price").toPlainString();
        return switch (row.getString("kind")) {
            case "book" -> new Book(title, author, price);
            case "ebook" -> new EBook(title, author, price);
            case "accessory" -> new Accessory(title, price);
            default -> throw new SQLException(
                    "unknown kind: " + row.getString("kind"));
        };
    }

    private static String kindOf(Product product) {
        return switch (product) {
            case EBook _ -> "ebook";       // before Book: it is one
            case Book _ -> "book";
            case Accessory _ -> "accessory";
        };
    }

    // ---- Connections, transactions, and the schema ----------------

    private <R> R run(Work<R> work) {
        try (Connection db = database.connect()) {
            return work.apply(db);
        } catch (SQLException e) {
            throw new DataAccessException(
                    "database error: " + e.getMessage(), e);
        }
    }

    /** Runs the work as one transaction: all of it, or none of it. */
    private <R> R inTransaction(Work<R> work) {
        return run(db -> {
            db.setAutoCommit(false);
            try {
                R result = work.apply(db);
                db.commit();
                return result;
            } catch (SQLException | RuntimeException e) {
                db.rollback();
                throw e;
            }
        });
    }

    private static void delete(Connection db, String id)
            throws SQLException {
        try (PreparedStatement lines = db.prepareStatement(
                     "DELETE FROM order_line WHERE order_id = ?");
             PreparedStatement order = db.prepareStatement(
                     "DELETE FROM orders WHERE id = ?")) {
            lines.setString(1, id);
            lines.executeUpdate();
            order.setString(1, id);
            order.executeUpdate();
        }
    }

    private void createTablesIfMissing() {
        run(db -> {
            try (Statement sql = db.createStatement()) {
                sql.execute("""
                        CREATE TABLE IF NOT EXISTS orders (
                            id        VARCHAR(20)   PRIMARY KEY,
                            level     VARCHAR(10)   NOT NULL,
                            total     DECIMAL(10,2) NOT NULL,
                            placed_at TIMESTAMP WITH TIME ZONE
                                      NOT NULL
                        )""");
                sql.execute("""
                        CREATE TABLE IF NOT EXISTS order_line (
                            order_id   VARCHAR(20)  NOT NULL
                                       REFERENCES orders(id),
                            line_no    INT          NOT NULL,
                            kind       VARCHAR(10)  NOT NULL,
                            title      VARCHAR(200) NOT NULL,
                            author     VARCHAR(100),
                            unit_price DECIMAL(10,2) NOT NULL,
                            quantity   INT          NOT NULL,
                            PRIMARY KEY (order_id, line_no)
                        )""");
            }
            return null;
        });
    }
}
