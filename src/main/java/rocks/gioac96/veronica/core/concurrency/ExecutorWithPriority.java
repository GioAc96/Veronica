package rocks.gioac96.veronica.core.concurrency;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class ExecutorWithPriority implements ExecutorService {

    private final PriorityExecutorService baseExecutor;
    private final int priority;

    protected ExecutorWithPriority(
        PriorityExecutorService baseExecutor,
        int priority
    ) {

        this.baseExecutor = baseExecutor;
        this.priority = priority;

    }

    @Override
    public void execute(Runnable command) {

        baseExecutor.execute(command, priority);

    }

    @Override
    public void shutdown() {

        baseExecutor.shutdown();

    }

    @Override
    public List<Runnable> shutdownNow() {

        return baseExecutor.shutdownNow();

    }

    @Override
    public boolean isShutdown() {

        return baseExecutor.isShutdown();

    }

    @Override
    public boolean isTerminated() {

        return baseExecutor.isTerminated();

    }

    @Override
    public boolean awaitTermination(
        long timeout,
        TimeUnit unit
    ) throws InterruptedException {

        return baseExecutor.awaitTermination(timeout, unit);

    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {

        return baseExecutor.submit(task, priority);

    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {

        return baseExecutor.submit(task, result);

    }

    @Override
    public Future<?> submit(Runnable task) {

        return baseExecutor.submit(task);

    }

    @Override
    public <T> List<Future<T>> invokeAll(
        Collection<? extends Callable<T>> tasks
    ) throws InterruptedException {

        return baseExecutor.invokeAll(tasks);

    }

    @Override
    public <T> List<Future<T>> invokeAll(
        Collection<? extends Callable<T>> tasks,
        long timeout,
        TimeUnit unit
    ) throws InterruptedException {

        return baseExecutor.invokeAll(tasks, timeout, unit);

    }

    @Override
    public <T> T invokeAny(
        Collection<? extends Callable<T>> tasks
    ) throws InterruptedException, ExecutionException {

        return baseExecutor.invokeAny(tasks);

    }

    @Override
    public <T> T invokeAny(
        Collection<? extends Callable<T>> tasks,
        long timeout,
        TimeUnit unit
    ) throws InterruptedException, ExecutionException, TimeoutException {

        return baseExecutor.invokeAny(tasks, timeout, unit);

    }

}
