package rocks.gioac96.veronica.core.concurrent;

import java.util.concurrent.Executor;

public interface PriorityExecutor<P extends Comparable<P>> extends Executor {

    void execute(Runnable task, P priority);

    void execute(PriorityTask<P> task);

}
