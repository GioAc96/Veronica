package rocks.gioac96.veronica.core.concurrency;

import lombok.Getter;

public abstract class PriorityTask implements HasPriority {

    @Getter
    private final int priority;

    protected PriorityTask(int priority) {

        this.priority = priority;

    }

}
