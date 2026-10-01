import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlTour {

    public static void main(String[] args) throws SQLException {
        // An in-memory H2 database: created now, gone when we close
        try (Connection db = DriverManager.getConnection(
                     "jdbc:h2:mem:tour");
             Statement sql = db.createStatement()) {

            // DDL: define tables
            sql.execute("""
                    CREATE TABLE author (
                        id    INT PRIMARY KEY,
                        name  VARCHAR(100) NOT NULL
                    )""");
            sql.execute("""
                    CREATE TABLE book (
                        id        INT PRIMARY KEY,
                        title     VARCHAR(200) NOT NULL,
                        price     DECIMAL(8, 2) NOT NULL,
                        author_id INT REFERENCES author(id)
                    )""");

            // DML: add, change, and remove rows
            sql.executeUpdate("""
                    INSERT INTO author VALUES
                        (1, 'Asha Rao'), (2, 'Dev Malhotra')""");
            sql.executeUpdate("""
                    INSERT INTO book VALUES
                        (1, 'The Quiet Library', 12.99, 1),
                        (2, 'Java for Everyone', 29.50, 2),
                        (3, 'Night Trains',      11.25, 1),
                        (4, 'Old Maps',           8.00, 1)""");
            int changed = sql.executeUpdate(
                    "UPDATE book SET price = price + 1"
                            + " WHERE price < 12");
            int removed = sql.executeUpdate(
                    "DELETE FROM book WHERE title = 'Old Maps'");
            System.out.println("Updated " + changed + ", deleted "
                    + removed);

            // Queries: filter, sort, join, group
            print(sql, "Under $20, cheapest first", """
                    SELECT title, price FROM book
                    WHERE price < 20 ORDER BY price""");
            print(sql, "Books with their authors", """
                    SELECT b.title, a.name
                    FROM book b JOIN author a ON b.author_id = a.id
                    ORDER BY b.title""");
            print(sql, "Books per author", """
                    SELECT a.name, COUNT(*) AS books, SUM(b.price)
                    FROM book b JOIN author a ON b.author_id = a.id
                    GROUP BY a.name ORDER BY a.name""");
        }
    }

    /** Runs a query and prints every row, columns separated by |. */
    static void print(Statement sql, String heading, String query)
            throws SQLException {
        System.out.println(heading + ":");
        try (ResultSet rows = sql.executeQuery(query)) {
            int columns = rows.getMetaData().getColumnCount();
            while (rows.next()) {
                StringBuilder line = new StringBuilder("  ");
                for (int c = 1; c <= columns; c++) {
                    line.append(c > 1 ? " | " : "")
                            .append(rows.getString(c));
                }
                System.out.println(line);
            }
        }
    }
}
