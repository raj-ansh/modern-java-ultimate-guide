import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PreparedStatements {

    static final String URL = "jdbc:h2:mem:shop";

    record Book(long id, String title, BigDecimal price) {}

    public static void main(String[] args) throws SQLException {
        try (Connection db = DriverManager.getConnection(URL)) {
            try (Statement sql = db.createStatement()) {
                sql.execute("""
                        CREATE TABLE book (
                            id    BIGINT AUTO_INCREMENT PRIMARY KEY,
                            title VARCHAR(200) NOT NULL,
                            price DECIMAL(8, 2) NOT NULL
                        )""");
            }

            // One statement, many executions: ? marks each parameter
            String insert =
                    "INSERT INTO book (title, price) VALUES (?, ?)";
            try (PreparedStatement add = db.prepareStatement(insert,
                    Statement.RETURN_GENERATED_KEYS)) {
                add.setString(1, "The Quiet Library");
                add.setBigDecimal(2, new BigDecimal("12.99"));
                add.executeUpdate();
                try (ResultSet keys = add.getGeneratedKeys()) {
                    keys.next();
                    System.out.println("New id: " + keys.getLong(1));
                }

                // A batch sends many rows in one round trip
                String[][] more = {{"Java for Everyone", "29.50"},
                        {"Tea, Toast and Tales", "9.75"},
                        {"Night Trains", "11.25"}};
                for (String[] row : more) {
                    add.setString(1, row[0]);
                    add.setBigDecimal(2, new BigDecimal(row[1]));
                    add.addBatch();
                }
                System.out.println("Batch inserted: "
                        + add.executeBatch().length);
            }

            // Read rows into records
            String query = "SELECT id, title, price FROM book "
                    + "WHERE price < ? ORDER BY price";
            try (PreparedStatement find =
                         db.prepareStatement(query)) {
                find.setBigDecimal(1, new BigDecimal("20"));
                try (ResultSet rows = find.executeQuery()) {
                    while (rows.next()) {
                        Book book = new Book(rows.getLong("id"),
                                rows.getString("title"),
                                rows.getBigDecimal("price"));
                        System.out.println(book);
                    }
                }
            }
        }
    }
}
