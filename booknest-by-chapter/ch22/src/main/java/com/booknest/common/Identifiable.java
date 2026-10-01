package com.booknest.common;

/**
 * Something with a unique identifier.
 *
 * @param <ID> the type of the identifier, such as String or Long
 */
public interface Identifiable<ID> {

    ID id();
}
