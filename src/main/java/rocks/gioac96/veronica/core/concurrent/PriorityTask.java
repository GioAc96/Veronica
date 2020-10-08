package rocks.gioac96.veronica.core.concurrent;

import lombok.Getter;

public class PriorityTask<P extends Comparable<P>> implements Runnable, Comparable<PriorityTask<P>> {

    @Getter
    private final P priority;

    @Getter
    private final Runnable task;

    protected PriorityTask(P priority, Runnable task) {

        this.priority = priority;

        this.task = task;
    }

    @Override
    public void run() {

        task.run();

    }

    @Override
    public int compareTo(PriorityTask<P> o) {

        return this.priority.compareTo(o.priority);

    }

}
