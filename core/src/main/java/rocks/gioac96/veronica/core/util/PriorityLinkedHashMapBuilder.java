package rocks.gioac96.veronica.core.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;

public final class PriorityLinkedHashMapBuilder<K, V> {

    private static final int DEFAULT_PRIORITY = Integer.MAX_VALUE;
    @Getter
    private final LinkedHashMap<K, V> draft = new LinkedHashMap<>();
    private final Map<K, Integer> entryPriorities = new HashMap<>();

    public boolean put(K key, V value, int priority) {

        if (draft.containsKey(key)) {

            if (priority <= entryPriorities.get(key)) {

                draft.put(key, value);
                entryPriorities.put(key, priority);
                return true;

            }

            return false;

        } else {

            draft.put(key, value);
            entryPriorities.put(key, priority);

            return true;

        }

    }

    public LinkedHashMap<K, V> toLinkedHashMap() {

        return draft;

    }


}
