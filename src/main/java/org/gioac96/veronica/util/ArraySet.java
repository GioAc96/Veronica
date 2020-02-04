package org.gioac96.veronica.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * Set based on ArrayList, for maximum efficiency on reading operations.
 *
 * @param <T> Type of the set
 */
public class ArraySet<T> extends SetOps<T> implements Set<T> {

    private ArrayList<T> entries;

    public ArraySet() {

        this.entries = new ArrayList<>();

    }

    /**
     * Initializes an {@link ArraySet} given its starting elements and their type.
     * @param type type of the {@link ArraySet}
     * @param elements starting elements of the set
     * @param <U> type of the {@link ArraySet}
     * @return the initialized {@link ArraySet}
     */
    public static <U> ArraySet<U> of(Class<U> type, U... elements) {

        ArraySet<U> result = new ArraySet<>();

        for (U element : elements) {

            result.add(element);

        }

        return result;

    }

    @Override
    public int size() {

        return entries.size();

    }

    @Override
    public Iterator<T> iterator() {

        return entries.iterator();

    }

    @Override
    public boolean add(T t) {

        if (contains(t)) {

            return false;

        }

        entries.add(t);

        return true;

    }

}
