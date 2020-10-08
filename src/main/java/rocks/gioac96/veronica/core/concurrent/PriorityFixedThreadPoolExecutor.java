package rocks.gioac96.veronica.core.concurrent;

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

public class PriorityFixedThreadPoolExecutor<P extends Comparable<P>>
    extends ThreadPoolExecutor
    implements PriorityExecutor<P> {

    private final P defaultPriority;

    protected PriorityFixedThreadPoolExecutor(
        PriorityFixedThreadPoolExecutorBuilder<P> b
    ) {

        super(
            b.poolSize,
            b.poolSize,
            b.keepAliveTime,
            b.keepAliveTimeUnit,
            (PriorityBlockingQueue)new PriorityBlockingQueue<PriorityTask<P>>()
        );

        this.defaultPriority = b.defaultPriority;

    }
    
    public static <P extends Comparable<P>> PriorityFixedThreadPoolExecutorBuilder<P> builder() {

        class PriorityFixedThreadPoolExecutorBuilderImpl<P1 extends Comparable<P1>>
            extends PriorityFixedThreadPoolExecutorBuilder<P1>
            implements BuildsMultipleInstances {

        }

        return new PriorityFixedThreadPoolExecutorBuilderImpl<>();

    }

    @Override
    public void execute(Runnable command) {

        execute(new PriorityTask<>(defaultPriority, command));

    }

    @Override
    public void execute(Runnable command, P priority) {

        super.execute(new PriorityTask<>(priority, command));

    }

    @Override
    public void execute(PriorityTask<P> task) {

        super.execute(task);

    }

    public abstract static class PriorityFixedThreadPoolExecutorBuilder<P extends Comparable<P>>
        extends Builder<PriorityFixedThreadPoolExecutor<P>> {

        private P defaultPriority;
        private int poolSize = Runtime.getRuntime().availableProcessors();
        private long keepAliveTime = 0;
        private TimeUnit keepAliveTimeUnit = TimeUnit.MILLISECONDS;
        private ThreadFactory threadFactory = Executors.defaultThreadFactory();
        private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.DiscardPolicy();

        @Override
        protected boolean isValid() {

            return super.isValid() &&
                defaultPriority != null &&
                keepAliveTimeUnit != null &&
                threadFactory != null &&
                rejectedExecutionHandler != null;

        }

        @Override
        protected PriorityFixedThreadPoolExecutor<P> instantiate() {

            return new PriorityFixedThreadPoolExecutor<>(this);

        }

        public PriorityFixedThreadPoolExecutorBuilder<P> poolSize(int poolSize) {

            this.poolSize = poolSize;
            return this;

        }

        public PriorityFixedThreadPoolExecutorBuilder<P> poolSize(@NonNull Provider<Integer> poolSize) {

            return poolSize(poolSize.provide());

        }

        public PriorityFixedThreadPoolExecutorBuilder<P> defaultPriority(@NonNull P defaultPriority) {

            this.defaultPriority = defaultPriority;
            return this;

        }

        public PriorityFixedThreadPoolExecutorBuilder<P> defaultPriority(@NonNull Provider<P> defaultPriority) {

            return defaultPriority(defaultPriority.provide());

        }

        public PriorityFixedThreadPoolExecutorBuilder<P> keepAliveTime(long keepAliveTime, @NonNull TimeUnit keepAliveTimeUnit) {

            this.keepAliveTime = keepAliveTime;
            this.keepAliveTimeUnit = keepAliveTimeUnit;

            return this;

        }

        public PriorityFixedThreadPoolExecutorBuilder<P> keepAliveTime(long keepAliveTime) {

            this.keepAliveTime = keepAliveTime;
            return this;

        }

        public PriorityFixedThreadPoolExecutorBuilder<P> keepAliveTime(@NonNull Provider<Long> keepAliveTime) {

            return keepAliveTime(keepAliveTime.provide());

        }

        public PriorityFixedThreadPoolExecutorBuilder<P> keepAliveTimeUnit(@NonNull TimeUnit keepAliveTimeUnit) {

            this.keepAliveTimeUnit = keepAliveTimeUnit;
            return this;

        }

        public PriorityFixedThreadPoolExecutorBuilder<P> keepAliveTimeUnit(@NonNull Provider<TimeUnit> keepAliveTimeUnit) {

            return keepAliveTimeUnit(keepAliveTimeUnit.provide());

        }

        public PriorityFixedThreadPoolExecutorBuilder<P> threadFactory(@NonNull ThreadFactory threadFactory) {

            this.threadFactory = threadFactory;
            return this;

        }

        public PriorityFixedThreadPoolExecutorBuilder<P> threadFactory(@NonNull Provider<ThreadFactory> threadFactory) {

            return threadFactory(threadFactory.provide());

        }

        public PriorityFixedThreadPoolExecutorBuilder<P> rejectedExecutionHandler(@NonNull RejectedExecutionHandler rejectedExecutionHandler) {

            this.rejectedExecutionHandler = rejectedExecutionHandler;
            return this;

        }

        public PriorityFixedThreadPoolExecutorBuilder<P> rejectedExecutionHandler(@NonNull Provider<RejectedExecutionHandler> rejectedExecutionHandler) {

            return rejectedExecutionHandler(rejectedExecutionHandler.provide());

        }

    }

}
