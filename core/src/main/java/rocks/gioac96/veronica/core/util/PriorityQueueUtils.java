package rocks.gioac96.veronica.core.util;

import java.util.LinkedHashMap;
import java.util.PriorityQueue;

public final class PriorityQueueUtils {

    public static <K, V> LinkedHashMap<K, V> transferEntriesToSortedLinkedHashMap(PriorityQueue<PriorityEntry<Tuple<K, V>>> priorityEntriesQueue) {

        LinkedHashMap<K, V> result = new LinkedHashMap<>(priorityEntriesQueue.size());

        while (!priorityEntriesQueue.isEmpty()) {

            PriorityEntry<Tuple<K, V>> entry = priorityEntriesQueue.poll();

            result.put(entry.getValue().getFirst(), entry.getValue().getSecond());

        }

        return result;


    }

}
