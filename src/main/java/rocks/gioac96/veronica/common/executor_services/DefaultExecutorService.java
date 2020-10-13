package rocks.gioac96.veronica.common.executor_services;

import java.util.concurrent.ExecutorService;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;

public class DefaultExecutorService
    extends Builder<ExecutorService>
    implements BuildsSingleInstance {

    @Override
    protected ExecutorService instantiate() {

        return new DefaultPriorityExecutorService()
            .provide()
            .getExecutorWithDefaultPriority();

    }

}
