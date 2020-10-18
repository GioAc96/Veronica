package rocks.gioac96.veronica.util;

import lombok.Getter;

@Getter
public class PriorityEntry<T> implements HasPriority {

    public static final int DEFAULT_PRIORITY = Integer.MAX_VALUE;

    private final T value;
    private final int priority;

    public PriorityEntry(T value, int priority) {

        this.value = value;
        this.priority = priority;

    }

    public PriorityEntry(T value) {

        this.value = value;
        this.priority = DEFAULT_PRIORITY;

    }

}
