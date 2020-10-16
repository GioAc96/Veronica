package rocks.gioac96.veronica.core.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.util.HasPriority;

@SuppressWarnings("unused")
public class PriorityFixedThreadPoolExecutor
    extends ThreadPoolExecutor
    implements PriorityExecutorService {

    @Getter
    private final int defaultPriority;

    protected PriorityFixedThreadPoolExecutor(
        PriorityFixedThreadPoolExecutorBuilder b
    ) {

        super(
            b.poolSize,
            b.poolSize,
            b.keepAliveTime,
            b.keepAliveTimeUnit,
            new PriorityBlockingQueue<>()
        );

        this.defaultPriority = b.defaultPriority;

    }

    public static PriorityFixedThreadPoolExecutorBuilder builder() {

        class PriorityFixedThreadPoolExecutorBuilderImpl
            extends PriorityFixedThreadPoolExecutorBuilder
            implements BuildsMultipleInstances {

        }

        return new PriorityFixedThreadPoolExecutorBuilderImpl();

    }

    private PriorityRunnableTask appendDefaultPriority(Runnable runnable) {

        if (runnable instanceof PriorityRunnableTask) {

            return (PriorityRunnableTask) runnable;

        } else {

            return new PriorityRunnableTask(defaultPriority, runnable);

        }

    }

    @Override
    public void execute(Runnable command) {

        if (command instanceof HasPriority) {

            super.execute(command);

        } else {

            super.execute(appendDefaultPriority(command));

        }

    }

    @Override
    public void execute(Runnable command, int priority) {

        super.execute(new PriorityRunnableTask(priority, command));

    }

    @Override
    public Future<?> submit(@NonNull Runnable task) {

        if (task instanceof HasPriority) {

            RunnableFuture<?> ftask = new PriorityFutureTask<>(task, null, ((HasPriority) task).getPriority());
            super.execute(ftask);

            return ftask;

        } else {

            return submit(task, defaultPriority);
        }

    }

    @Override
    public Future<?> submit(@NonNull Runnable task, int priority) {

        PriorityFutureTask<?> ftask = new PriorityFutureTask<>(task, null, priority);

        super.execute(ftask);

        return ftask;

    }

    @Override
    public <T> Future<T> submit(@NonNull Callable<T> task) {

        if (task instanceof HasPriority) {

            PriorityFutureTask<T> ftask = new PriorityFutureTask<>(task, ((HasPriority) task).getPriority());

            super.execute(ftask);

            return ftask;

        } else {

            return submit(task, defaultPriority);

        }

    }

    @Override
    public <T> Future<T> submit(@NonNull Callable<T> task, int priority) {

        PriorityFutureTask<T> ftask = new PriorityFutureTask<>(task, priority);

        super.execute(ftask);

        return ftask;

    }

    @Override
    public <T> Future<T> submit(@NonNull Runnable task, T result) {

        if (task instanceof HasPriority) {

            PriorityFutureTask<T> ftask = new PriorityFutureTask<>(task, result, ((HasPriority) task).getPriority());

            super.execute(ftask);

            return ftask;

        } else {

            return submit(task, result, defaultPriority);

        }

    }

    @Override
    public <T> Future<T> submit(@NonNull Runnable task, T result, int priority) {

        PriorityFutureTask<T> ftask = new PriorityFutureTask<>(task, result, priority);

        super.execute(ftask);

        return ftask;

    }

    @Override
    public ExecutorService getExecutorWithPriority(int priority) {

        return new ExecutorWithPriority(this, priority);

    }

    @Override
    public ExecutorService getExecutorWithDefaultPriority() {

        return getExecutorWithPriority(defaultPriority);

    }

    public abstract static class PriorityFixedThreadPoolExecutorBuilder
        extends Builder<PriorityFixedThreadPoolExecutor> {

        private int defaultPriority = Integer.MAX_VALUE;
        private int poolSize = Runtime.getRuntime().availableProcessors();
        private long keepAliveTime = 0;
        private TimeUnit keepAliveTimeUnit = TimeUnit.MILLISECONDS;
        private ThreadFactory threadFactory = Executors.defaultThreadFactory();
        private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.DiscardPolicy();

        @Override
        protected boolean isValid() {

            return super.isValid() &&
                keepAliveTimeUnit != null &&
                threadFactory != null &&
                rejectedExecutionHandler != null;

        }

        @Override
        protected PriorityFixedThreadPoolExecutor instantiate() {

            return new PriorityFixedThreadPoolExecutor(this);

        }

        public PriorityFixedThreadPoolExecutorBuilder poolSize(int poolSize) {

            this.poolSize = poolSize;
            return this;

        }

        public PriorityFixedThreadPoolExecutorBuilder poolSize(@NonNull Provider<Integer> poolSize) {

            return poolSize(poolSize.provide());

        }

        public PriorityFixedThreadPoolExecutorBuilder defaultPriority(int defaultPriority) {

            this.defaultPriority = defaultPriority;
            return this;

        }

        public PriorityFixedThreadPoolExecutorBuilder defaultPriority(@NonNull Provider<Integer> defaultPriority) {

            return defaultPriority(defaultPriority.provide());

        }

        public PriorityFixedThreadPoolExecutorBuilder keepAliveTime(long keepAliveTime, @NonNull TimeUnit keepAliveTimeUnit) {

            this.keepAliveTime = keepAliveTime;
            this.keepAliveTimeUnit = keepAliveTimeUnit;

            return this;

        }

        public PriorityFixedThreadPoolExecutorBuilder keepAliveTime(long keepAliveTime) {

            this.keepAliveTime = keepAliveTime;
            return this;

        }

        public PriorityFixedThreadPoolExecutorBuilder keepAliveTime(@NonNull Provider<Long> keepAliveTime) {

            return keepAliveTime(keepAliveTime.provide());

        }

        public PriorityFixedThreadPoolExecutorBuilder keepAliveTimeUnit(@NonNull TimeUnit keepAliveTimeUnit) {

            this.keepAliveTimeUnit = keepAliveTimeUnit;
            return this;

        }

        public PriorityFixedThreadPoolExecutorBuilder keepAliveTimeUnit(@NonNull Provider<TimeUnit> keepAliveTimeUnit) {

            return keepAliveTimeUnit(keepAliveTimeUnit.provide());

        }

        public PriorityFixedThreadPoolExecutorBuilder threadFactory(@NonNull ThreadFactory threadFactory) {

            this.threadFactory = threadFactory;
            return this;

        }

        public PriorityFixedThreadPoolExecutorBuilder threadFactory(@NonNull Provider<ThreadFactory> threadFactory) {

            return threadFactory(threadFactory.provide());

        }

        public PriorityFixedThreadPoolExecutorBuilder rejectedExecutionHandler(@NonNull RejectedExecutionHandler rejectedExecutionHandler) {

            this.rejectedExecutionHandler = rejectedExecutionHandler;
            return this;

        }

        public PriorityFixedThreadPoolExecutorBuilder rejectedExecutionHandler(@NonNull Provider<RejectedExecutionHandler> rejectedExecutionHandler) {

            return rejectedExecutionHandler(rejectedExecutionHandler.provide());

        }

    }

}
