package rocks.gioac96.veronica.core.common.executor_services;

import rocks.gioac96.veronica.core.concurrency.PriorityFixedThreadPoolExecutor;
import rocks.gioac96.veronica.core.providers.Singleton;

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
