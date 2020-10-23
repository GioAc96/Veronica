package rocks.gioac96.veronica.core.concurrency;

import java.util.concurrent.Callable;
import rocks.gioac96.veronica.core.util.PriorityEntry;

public class PriorityCallableTask<V> extends PriorityEntry<Callable<V>> implements Callable<V> {

    protected PriorityCallableTask(int priority, Callable<V> task) {

        super(task, priority);

    }

    @Override
    public V call() throws Exception {

        return getValue().call();

    }

}