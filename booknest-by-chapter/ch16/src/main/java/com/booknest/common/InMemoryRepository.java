package com.booknest.common;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A repository that keeps its items in memory, in a growing array.
 * Good for tests and small programs; a database comes in Chapter 25.
 */
public class InMemoryRepository<T extends Identifiable<ID>, ID>
        implements Repository<T, ID> {

    // Object[], not T[]: Java can't create arrays of a type parameter
    private Object[] items = new Object[4];
    private int size = 0;

    @Override
    public void save(T item) {
        Objects.requireNonNull(item, "item must not be null");
        int index = indexOf(item.id());
        if (index >= 0) {
            items[index] = item;
            return;
        }
        if (size == items.length) {
            items = Arrays.copyOf(items, size * 2);
        }
        items[size] = item;
        size++;
    }

    @Override
    public boolean existsById(ID id) {
        return indexOf(id) >= 0;
    }

    @Override
    public T getById(ID id) {
        int index = indexOf(id);
        if (index < 0) {
            throw new NoSuchElementException("no item with id " + id);
        }
        return itemAt(index);
    }

    @Override
    public int count() {
        return size;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        for (int i = 0; i < size; i++) {
            action.accept(itemAt(i));
        }
    }

    @Override
    public int countMatching(Predicate<? super T> test) {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (test.test(itemAt(i))) {
                count++;
            }
        }
        return count;
    }

    private int indexOf(ID id) {
        for (int i = 0; i < size; i++) {
            if (itemAt(i).id().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked") // only T objects are ever stored
    private T itemAt(int index) {
        return (T) items[index];
    }
}
