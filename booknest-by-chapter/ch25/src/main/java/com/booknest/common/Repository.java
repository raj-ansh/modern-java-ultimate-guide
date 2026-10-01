package com.booknest.common;

import java.util.NoSuchElementException;
import java.util.Optional;
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

    /** Returns the item with this id, or an empty Optional. */
    Optional<T> findById(ID id);

    /** Returns the number of stored items. */
    int count();

    /** Removes every stored item. */
    void deleteAll();

    /** Returns a stream of the stored items, in insertion order. */
    Stream<T> stream();

    /**
     * Returns the item with this id, for callers that know it exists.
     *
     * @throws NoSuchElementException if there is none
     */
    default T getById(ID id) {
        return findById(id).orElseThrow(() ->
                new NoSuchElementException("no item with id " + id));
    }

    /** Returns true if an item with this id is stored. */
    default boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    /** Passes every stored item to the action, in insertion order. */
    default void forEach(Consumer<? super T> action) {
        stream().forEach(action);
    }

    /** Counts the stored items that pass the test. */
    default int countMatching(Predicate<? super T> test) {
        return Math.toIntExact(stream().filter(test).count());
    }
}
