package rocks.gioac96.veronica.common.executor_services;

import java.util.concurrent.ExecutorService;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;

public class ServerExecutor
    extends Builder<ExecutorService>
    implements BuildsSingleInstance {

    public static final int SERVER_TASKS_PRIORITY = 0;

    @Override
    protected ExecutorService instantiate() {

        return new DefaultPriorityExecutorService()
            .provide()
            .getExecutorWithPriority(SERVER_TASKS_PRIORITY);

    }

}
