package com.booknest.common;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Stores and finds objects by their identifier.
 *
 * @param <T>  the type of object stored
 * @param <ID> the type of the object's identifier
 */
public interface Repository<T extends Identifiable<ID>, ID> {

    /** Stores the item, replacing any item with the same id. */
    void save(T item);

    /** Returns true if an item with this id is stored. */
    boolean existsById(ID id);

    /**
     * Returns the item with this id.
     *
     * @throws java.util.NoSuchElementException if there is none
     */
    T getById(ID id);

    /** Returns the number of stored items. */
    int count();

    /** Passes every stored item to the action, in insertion order. */
    void forEach(Consumer<? super T> action);

    /** Counts the stored items that pass the test. */
    int countMatching(Predicate<? super T> test);
}
