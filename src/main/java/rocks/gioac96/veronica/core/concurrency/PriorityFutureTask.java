package rocks.gioac96.veronica.core.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import lombok.Getter;

public class PriorityFutureTask<V> extends FutureTask<V> implements HasPriority {

    @Getter
    private final int priority;

    public PriorityFutureTask(Callable<V> callable, int priority) {

        super(callable);

        this.priority = priority;

    }

    public PriorityFutureTask(Runnable runnable, V result, int priority) {

        super(runnable, result);

        this.priority = priority;

    }

}
