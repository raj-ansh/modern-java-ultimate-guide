package com.booknest.common;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

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

    /** Returns a stream of the stored items, in insertion order. */
    Stream<T> stream();

    /** Passes every stored item to the action, in insertion order. */
    default void forEach(Consumer<? super T> action) {
        stream().forEach(action);
    }

    /** Counts the stored items that pass the test. */
    default int countMatching(Predicate<? super T> test) {
        return Math.toIntExact(stream().filter(test).count());
    }
}
