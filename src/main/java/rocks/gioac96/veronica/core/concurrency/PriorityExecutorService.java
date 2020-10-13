package rocks.gioac96.veronica.core.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import lombok.NonNull;

public interface PriorityExecutorService extends ExecutorService {

    void execute(Runnable command, int priority);

    Future<?> submit(@NonNull Runnable task, int priority);

    <T> Future<T> submit(@NonNull Callable<T> task, int priority);

    <T> Future<T> submit(@NonNull Runnable task, T result, int priority);

    ExecutorService getExecutorWithPriority(int priority);

    ExecutorService getExecutorWithDefaultPriority();
}
