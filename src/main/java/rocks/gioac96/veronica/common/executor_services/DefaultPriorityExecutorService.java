package rocks.gioac96.veronica.common.executor_services;

import rocks.gioac96.veronica.core.concurrency.PriorityFixedThreadPoolExecutor;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;

public class DefaultPriorityExecutorService
    extends PriorityFixedThreadPoolExecutor.PriorityFixedThreadPoolExecutorBuilder
    implements BuildsSingleInstance {

    @Override
    protected void configure() {

        super.configure();

        defaultPriority(Integer.MAX_VALUE);

    }

}
