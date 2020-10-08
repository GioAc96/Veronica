package rocks.gioac96.veronica.core.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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

    private static void sleep() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static PriorityFixedThreadPoolExecutorBuilder builder() {

        class PriorityFixedThreadPoolExecutorBuilderImpl
            extends PriorityFixedThreadPoolExecutorBuilder
            implements BuildsMultipleInstances {

        }

        return new PriorityFixedThreadPoolExecutorBuilderImpl();

    }

    @Override
    public void execute(Runnable command) {

        if (command instanceof PriorityTask) {

            super.execute(command);

        } else {

            super.execute(new PriorityTask(defaultPriority, command));

        }


    }

    public void execute(Runnable command, int priority) {

        super.execute(new PriorityTask(priority, command));

    }

    public Executor getExecutorWithPriority(int priority) {

        class ExecutorImpl implements Executor {

            private final PriorityFixedThreadPoolExecutor baseExecutor;
            private final int priority;

            private ExecutorImpl(PriorityFixedThreadPoolExecutor baseExecutor, int priority) {

                this.baseExecutor = baseExecutor;
                this.priority = priority;

            }

            @Override
            public void execute(Runnable command) {

                baseExecutor.execute(command, priority);

            }

        }

        return new ExecutorImpl(this, priority);

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
