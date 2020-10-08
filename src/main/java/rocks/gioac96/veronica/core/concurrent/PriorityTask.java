package rocks.gioac96.veronica.core.concurrent;

import lombok.Getter;

public class PriorityTask implements Runnable, Comparable<PriorityTask> {

    @Getter
    private final int priority;

    @Getter
    private final Runnable task;

    protected PriorityTask(int priority, Runnable task) {

        this.priority = priority;

        this.task = task;
    }

    @Override
    public void run() {

        task.run();

    }

    @Override
    public int compareTo(PriorityTask o) {

        return Integer.compare(this.priority, o.priority);

    }

}
