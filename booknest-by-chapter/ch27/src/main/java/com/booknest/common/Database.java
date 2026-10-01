package com.booknest.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Where BookNest's database is. Each call to connect() opens a new
 * connection, which the caller must close.
 *
 * @param url a JDBC URL, such as "jdbc:h2:mem:test"
 */
public record Database(String url) {

    public Database {
        Objects.requireNonNull(url, "url must not be null");
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
