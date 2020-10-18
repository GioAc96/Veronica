package rocks.gioac96.veronica.common.executor_services;

import java.util.concurrent.ExecutorService;
import rocks.gioac96.veronica.common.CommonExecutorServices;
import rocks.gioac96.veronica.providers.ConfigurableProvider;
import rocks.gioac96.veronica.providers.Singleton;

public class ServerExecutorService
    extends ConfigurableProvider<ExecutorService>
    implements Singleton {

    public static final int SERVER_TASKS_PRIORITY = 0;
    @Override
    protected ExecutorService instantiate() {

        return CommonExecutorServices
            .defaultPriorityExecutorService()
            .getExecutorWithPriority(SERVER_TASKS_PRIORITY);

    }

}
