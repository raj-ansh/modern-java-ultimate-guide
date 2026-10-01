package com.booknest.common;

/**
 * A storage operation failed: the database is unreachable, or a
 * statement was rejected. Unchecked, because callers of a repository
 * usually can't recover from it, and shouldn't depend on JDBC.
 */
public class DataAccessException extends RuntimeException {

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
