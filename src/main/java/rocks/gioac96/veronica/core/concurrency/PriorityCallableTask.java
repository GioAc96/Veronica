package rocks.gioac96.veronica.core.concurrency;

import java.util.concurrent.Callable;
import lombok.Getter;

public class PriorityCallableTask<V> extends PriorityTask implements Callable<V> {

    @Getter
    private final Callable<V> task;

    protected PriorityCallableTask(int priority, Callable<V> task) {

        super(priority);

        this.task = task;

    }


    @Override
    public V call() throws Exception {

        return task.call();

    }

}
