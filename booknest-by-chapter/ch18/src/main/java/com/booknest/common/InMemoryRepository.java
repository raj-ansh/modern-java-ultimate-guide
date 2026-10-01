package com.booknest.common;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A repository that keeps its items in memory.
 * Good for tests and small programs; a database comes in Chapter 25.
 */
public class InMemoryRepository<T extends Identifiable<ID>, ID>
        implements Repository<T, ID> {

    // Fast lookup by id, and remembers the order items were saved in
    private final Map<ID, T> items = new LinkedHashMap<>();

    @Override
    public void save(T item) {
        Objects.requireNonNull(item, "item must not be null");
        items.put(item.id(), item);
    }

    @Override
    public boolean existsById(ID id) {
        return items.containsKey(id);
    }

    @Override
    public T getById(ID id) {
        T item = items.get(id);
        if (item == null) {
            throw new NoSuchElementException("no item with id " + id);
        }
        return item;
    }

    @Override
    public int count() {
        return items.size();
    }

    @Override
    public Stream<T> stream() {
        return items.values().stream();
    }
}
