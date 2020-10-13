package rocks.gioac96.veronica.common.executor_services;

import java.util.concurrent.ExecutorService;
import rocks.gioac96.veronica.common.CommonExecutorServices;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;

public class ServerExecutorService
    extends Builder<ExecutorService>
    implements BuildsSingleInstance {

    public static final int SERVER_TASKS_PRIORITY = 0;

    @Override
    protected ExecutorService instantiate() {

        return CommonExecutorServices
            .defaultPriorityExecutorService()
            .getExecutorWithPriority(SERVER_TASKS_PRIORITY);

    }

}
