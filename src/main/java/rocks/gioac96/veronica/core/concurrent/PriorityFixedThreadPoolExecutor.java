package rocks.gioac96.veronica.core.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.NonNull;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.Provider;

public class PriorityFixedThreadPoolExecutor
    extends ThreadPoolExecutor {

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

    public <T> Future<T> submit(@NonNull Callable<T> task, int priority) {

        PriorityFutureTask<T> ftask = new PriorityFutureTask<T>(task, priority);

        super.execute(ftask);

        return ftask;

    }

    @Override
    public <T> Future<T> submit(@NonNull Runnable task, T result) {

        if (task instanceof HasPriority) {

            PriorityFutureTask<T> ftask = new PriorityFutureTask<T>(task, result, ((HasPriority) task).getPriority());

            super.execute(ftask);

            return ftask;

        } else {

            return submit(task, result, defaultPriority);

        }

    }

    public <T> Future<T> submit(@NonNull Runnable task, T result, int priority) {

        PriorityFutureTask<T> ftask = new PriorityFutureTask<T>(task, result, priority);

        super.execute(ftask);

        return ftask;

    }


    public Executor getExecutorWithPriority(int priority) {

        class FixedPriorityExecutorImpl implements ExecutorService {

            private final PriorityFixedThreadPoolExecutor baseExecutor;
            private final int priority;

            private FixedPriorityExecutorImpl(
                PriorityFixedThreadPoolExecutor baseExecutor,
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

                return baseExecutor.submit(task);

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

        return new FixedPriorityExecutorImpl(this, priority);

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
