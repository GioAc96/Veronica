package rocks.gioac96.veronica.core.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import rocks.gioac96.veronica.core.providers.CreationException;

public final class PriorityListBuilder<T> {

    private final Map<Integer, List<T>> priorityLists = new HashMap<>();
    private static final int DEFAULT_PRIORITY = Integer.MAX_VALUE;
    private int entriesCount = 0;

    public void put(T element) {

        put(element, DEFAULT_PRIORITY);

    }

    public void put(T element, Integer priority) {

        priorityLists.computeIfAbsent(priority, p -> new LinkedList<>()).add(element);
        entriesCount++;

    }

    public ArrayList<T> toArrayList() {

        ArrayList<T> arrayList = new ArrayList<>(entriesCount);

        appendEntriesToList(arrayList);

        return arrayList;

    }

    public LinkedList<T> toLinkedList() {

        LinkedList<T> linkedList = new LinkedList<>();

        appendEntriesToList(linkedList);

        return linkedList;

    }

    private void appendEntriesToList(List<T>  list) throws CreationException {

        priorityLists
            .entrySet()
            .stream()
            .sorted(Comparator.comparingInt(Map.Entry::getKey))
            .forEachOrdered(entry -> {

                entry.getValue().forEach(list::add);

            });

    }

}
