package rocks.gioac96.veronica.common.executor_services;

import rocks.gioac96.veronica.concurrency.PriorityFixedThreadPoolExecutor;
import rocks.gioac96.veronica.providers.Singleton;

/**
 * Framework's default priority executor service.
 */
public class DefaultPriorityExecutorService
    extends PriorityFixedThreadPoolExecutor.PriorityFixedThreadPoolExecutorBuilder
    implements Singleton {

    @Override
    protected void configure() {

        poolSize(Runtime.getRuntime().availableProcessors());
        defaultPriority(Integer.MAX_VALUE);

        super.configure();

    }

}
