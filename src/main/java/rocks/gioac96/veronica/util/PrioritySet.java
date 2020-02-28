package rocks.gioac96.veronica.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import lombok.Getter;

/**
 * Set with support for elements priority.
 *
 * @param <T> the type of the elements in the set
 */
public class PrioritySet<T> extends SetOps<T> implements Set<T>, Collection<T> {

    private static final int DEFAULT_PRIORITY = 0;
    private ArrayList<Entry> entries;

    public PrioritySet() {

        entries = new ArrayList<>();

    }

    /**
     * Initializes an {@link PrioritySet} given its starting elements and their type.
     *
     * @param elements starting elements of the set
     * @param <U>      type of the {@link PrioritySet}
     * @return the initialized {@link PrioritySet}
     */
    @SafeVarargs
    public static <U> PrioritySet<U> of(U... elements) {

        PrioritySet<U> result = new PrioritySet<>();

        Collections.addAll(result, elements);

        return result;

    }

    @Override
    public int size() {

        return entries.size();

    }

    @Override
    public Iterator<T> iterator() {

        return new Iterator<T>() {

            Iterator<Entry> entriesIterator = entriesIterator();

            @Override
            public boolean hasNext() {

                return entriesIterator.hasNext();

            }

            @Override
            public T next() {

                return entriesIterator.next().element;

            }

            public void remove() {

                entriesIterator.remove();

            }


        };

    }

    @Override
    public boolean add(T t) {

        return add(t, DEFAULT_PRIORITY);

    }

    /**
     * Adds an element to the set with a specified priority. If the set already contains the
     * element, the element is ignored and the method returns false.
     *
     * @param element  element to add to the set
     * @param priority priority to assign to the element
     * @return true iff the element was not already in the set.
     */
    public boolean add(T element, int priority) {

        if (contains(element)) {

            return false;

        }

        int insertIndex = 0;

        for (Entry entry : entries) {

            if (entry.priority > priority) {

                break;

            }

            insertIndex++;

        }

        entries.add(insertIndex, new Entry(priority, element));

        return true;

    }

    /**
     * Gets priority of the specified element.
     *
     * @param element element to get the priority of
     * @return the priority of the specified element, null if the element is not in the set.
     */
    public Integer getPriority(T element) {

        for (Entry entry : entries) {

            if (entry.element.equals(element)) {

                return entry.priority;

            }

        }

        return null;

    }

    /**
     * Changes the priority of an element in a set. If the element is not in the set, the element
     * is added to the set.
     *
     * @param element  element to change the priority of
     * @param priority priority to assign to the element
     * @return true iff the element was not in the set already or if the element had a different
     *      priority prior to the call.
     */
    public boolean changePriority(T element, int priority) {

        Iterator<Entry> entriesIterator = entriesIterator();

        while (entriesIterator.hasNext()) {

            Entry next = entriesIterator.next();

            if (next.element.equals(element)) {

                if (priority == next.priority) {

                    return false;

                } else {

                    entriesIterator.remove();

                    return add(element, priority);

                }

            }

        }

        return add(element, priority);

    }

    public Iterator<Entry> entriesIterator() {

        return entries.iterator();

    }

    public boolean addAll(Collection<? extends T> elements) {

        if (elements == null) {

            return false;

        }

        boolean changed = false;

        Iterator<? extends T> elementsIterator = elements.iterator();

        while (elementsIterator.hasNext()) {

            if (add(elementsIterator.next())) {

                changed = true;
                break;

            }

        }

        while (elementsIterator.hasNext()) {

            add(elementsIterator.next());

        }

        return changed;

    }

    public boolean addAll(PrioritySet<? extends T> elements) {

        if (elements == null) {

            return false;

        }

        boolean changed = false;

        Iterator<Entry> entriesIterator = entriesIterator();

        while (entriesIterator.hasNext()) {

            Entry next = entriesIterator.next();

            if (changePriority(next.element, next.priority)) {

                changed = true;

                break;

            }

        }

        while (entriesIterator.hasNext()) {

            Entry next = entriesIterator.next();
            changePriority(next.element, next.priority);

        }

        return changed;

    }

    private class Entry {

        @Getter
        private final int priority;

        @Getter
        private final T element;

        public Entry(int priority, T element) {
            this.priority = priority;
            this.element = element;
        }

    }

}
