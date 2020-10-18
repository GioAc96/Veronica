package rocks.gioac96.veronica.core.common.executor_services;

import java.util.concurrent.ExecutorService;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.Singleton;

/**
 * Framework's default executor service.
 */
public class DefaultExecutorService
    extends ConfigurableProvider<ExecutorService>
    implements Singleton {

    protected ExecutorService instantiate() {

        return new DefaultPriorityExecutorService()
            .provide()
            .getExecutorWithDefaultPriority();

    }

}
