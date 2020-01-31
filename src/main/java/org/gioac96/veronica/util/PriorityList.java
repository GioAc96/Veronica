package org.gioac96.veronica.util;

import java.util.ArrayList;
import java.util.Iterator;
import lombok.Getter;

public class PriorityList<T> implements Iterable<T> {

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

    private ArrayList<Entry> entries;

    public PriorityList() {

        entries = new ArrayList<>();

    }

    public int size() {

        return entries.size();

    }

    public boolean isEmpty() {

        return entries.isEmpty();

    }

    public boolean contains(T element) {

        for (Entry entry : entries) {

            if (entry.getElement().equals(element)) {

                return true;

            }

        }

        return false;

    }

    @Override
    public Iterator<T> iterator() {

        return new Iterator<T>() {

            Iterator<Entry> entriesIterator = entries.iterator();

            @Override
            public boolean hasNext() {

                return entriesIterator.hasNext();

            }

            @Override
            public T next() {

                return entriesIterator.next().getElement();

            }

        };

    }

    public Object[] toArray() {

        Object[] result = new Object[entries.size()];

        int i = 0;

        for (Entry entry : entries) {

            result[i] = entry.getElement();
            i++;

        }

        return result;

    }

    public boolean add(T element, int priority) {

        int insertIndex = 0;

        for (Entry entry : entries) {

            if (entry.getElement().equals(element)) {

                return false;

            }

            if (entry.priority > priority) {

                break;

            }

            insertIndex++;

        }

        entries.add(insertIndex, new Entry(priority, element));

        return true;

    }

    public boolean remove(T element) {

        int i = 0;

        for (Entry entry : entries) {

            if (entry.getElement().equals(element)) {

                entries.remove(i);
                return true;

            }

            i++;

        }

        return false;

    }

    public void clear() {

        entries.clear();

    }
}
