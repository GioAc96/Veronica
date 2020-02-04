package org.gioac96.veronica.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class ArraySet<T> extends SetOps<T> implements Set<T> {

    private ArrayList<T> entries;

    public ArraySet() {

        this.entries = new ArrayList<>();

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
