import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlInjection {

    static final String URL = "jdbc:h2:mem:orders";

    public static void main(String[] args) throws SQLException {
        try (Connection db = DriverManager.getConnection(URL);
             Statement sql = db.createStatement()) {
            sql.execute("""
                    CREATE TABLE orders (
                        id       VARCHAR(10) PRIMARY KEY,
                        customer VARCHAR(100) NOT NULL,
                        total    DECIMAL(8, 2) NOT NULL
                    )""");
            sql.executeUpdate("""
                    INSERT INTO orders VALUES
                        ('BN-1001', 'asha@example.com', 28.89),
                        ('BN-1002', 'ben@example.com',  17.87),
                        ('BN-1003', 'chen@example.com', 43.93)""");

            // What a customer types into "Find my orders"
            String honest = "asha@example.com";
            String attack = "nobody' OR '1'='1";

            System.out.println("Concatenated, honest input:");
            unsafeFind(sql, honest);
            System.out.println("Concatenated, attacker's input:");
            unsafeFind(sql, attack);

            System.out.println("Prepared, attacker's input:");
            safeFind(db, attack);
        }
    }

    // DON'T: user input becomes part of the SQL text
    static void unsafeFind(Statement sql, String email)
            throws SQLException {
        String query = "SELECT id, total FROM orders "
                + "WHERE customer = '" + email + "'";
        try (ResultSet rows = sql.executeQuery(query)) {
            printRows(rows);
        }
    }

    // DO: user input is only ever a parameter value
    static void safeFind(Connection db, String email)
            throws SQLException {
        String query =
                "SELECT id, total FROM orders WHERE customer = ?";
        try (PreparedStatement find = db.prepareStatement(query)) {
            find.setString(1, email);
            try (ResultSet rows = find.executeQuery()) {
                printRows(rows);
            }
        }
    }

    static void printRows(ResultSet rows) throws SQLException {
        int count = 0;
        while (rows.next()) {
            System.out.println("  " + rows.getString("id") + "  $"
                    + rows.getBigDecimal("total"));
            count++;
        }
        System.out.println("  " + count
                + (count == 1 ? " row" : " rows"));
    }
}
