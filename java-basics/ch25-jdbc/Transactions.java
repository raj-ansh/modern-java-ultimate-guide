import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Transactions {

    static final String URL = "jdbc:h2:mem:stock";

    public static void main(String[] args) throws SQLException {
        try (Connection db = DriverManager.getConnection(URL)) {
            try (Statement sql = db.createStatement()) {
                sql.execute("""
                        CREATE TABLE stock (
                            title     VARCHAR(100) PRIMARY KEY,
                            available INT NOT NULL
                                      CHECK (available >= 0)
                        )""");
                sql.execute("CREATE TABLE sale (title VARCHAR(100))");
                sql.executeUpdate("INSERT INTO stock "
                        + "VALUES ('Reading lamp', 2)");
            }

            // Without a transaction: each statement commits at once
            sell(db, 3);                        // fails half-way
            report(db, "After a failed sale, no transaction");

            // With a transaction: all or nothing
            db.setAutoCommit(false);
            try {
                sell(db, 3);
                db.commit();
            } catch (SQLException e) {
                db.rollback();                  // undo every step
                System.out.println("Rolled back: sale of 3 refused");
            } finally {
                db.setAutoCommit(true);
            }
            report(db, "After a rolled-back sale");
        }
    }

    /** Records the sale, then takes the items out of stock. */
    static void sell(Connection db, int quantity)
            throws SQLException {
        try (PreparedStatement record = db.prepareStatement(
                     "INSERT INTO sale VALUES ('Reading lamp')");
             PreparedStatement take = db.prepareStatement("""
                     UPDATE stock SET available = available - ?
                     WHERE title = 'Reading lamp'""")) {
            record.executeUpdate();             // step 1
            take.setInt(1, quantity);
            try {
                take.executeUpdate();           // step 2: CHECK fails
            } catch (SQLException e) {
                if (db.getAutoCommit()) {
                    System.out.println("Step 2 failed: "
                            + e.getSQLState() + " (check violated)");
                    return;                 // step 1 already saved
                }
                throw e;
            }
        }
    }

    static void report(Connection db, String when)
            throws SQLException {
        try (Statement sql = db.createStatement();
             ResultSet rows = sql.executeQuery("""
                     SELECT (SELECT available FROM stock),
                            (SELECT COUNT(*) FROM sale)""")) {
            rows.next();
            System.out.println(when + ": " + rows.getInt(1)
                    + " in stock, " + rows.getInt(2)
                    + " sales recorded");
        }
    }
}
