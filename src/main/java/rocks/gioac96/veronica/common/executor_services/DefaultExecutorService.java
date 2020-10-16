package rocks.gioac96.veronica.common.executor_services;

import java.util.concurrent.ExecutorService;
import rocks.gioac96.veronica.providers.ConfigurableProvider;
import rocks.gioac96.veronica.providers.Singleton;

/**
 * Framework's default executor service.
 */
public class DefaultExecutorService
    extends ConfigurableProvider<ExecutorService>
    implements Singleton {

    @Override
    protected boolean isValid() {

        return true;

    }

    protected ExecutorService instantiate() {

        return new DefaultPriorityExecutorService()
            .provide()
            .getExecutorWithDefaultPriority();

    }

}
