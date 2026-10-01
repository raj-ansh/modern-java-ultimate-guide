package com.booknest.common;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A repository that keeps its items in memory.
 * Good for tests and small programs; a database comes in Chapter 25.
 * Thread-safe: every method locks this repository, and stream()
 * works on a snapshot, so callers never iterate the live map.
 */
public class InMemoryRepository<T extends Identifiable<ID>, ID>
        implements Repository<T, ID> {

    // Fast lookup by id, and remembers the order items were saved in
    private final Map<ID, T> items = new LinkedHashMap<>();

    @Override
    public synchronized void save(T item) {
        Objects.requireNonNull(item, "item must not be null");
        items.put(item.id(), item);
    }

    @Override
    public synchronized Optional<T> findById(ID id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public synchronized int count() {
        return items.size();
    }

    @Override
    public synchronized void deleteAll() {
        items.clear();
    }

    @Override
    public synchronized Stream<T> stream() {
        return List.copyOf(items.values()).stream();
    }
}
