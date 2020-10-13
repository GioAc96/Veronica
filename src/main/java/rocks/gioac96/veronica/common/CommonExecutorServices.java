package rocks.gioac96.veronica.common;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import rocks.gioac96.veronica.common.executor_services.DefaultExecutorService;
import rocks.gioac96.veronica.common.executor_services.DefaultPriorityExecutorService;
import rocks.gioac96.veronica.common.executor_services.ServerExecutor;
import rocks.gioac96.veronica.core.concurrency.PriorityExecutorService;
import rocks.gioac96.veronica.providers.Provider;

public class CommonExecutorServices {

    private static final Provider<? extends PriorityExecutorService> defaultPriorityExecutorService
        = new DefaultPriorityExecutorService();
    private static final Provider<ExecutorService> defaultExecutorService
        = new DefaultExecutorService();
    private static final Provider<ExecutorService> serverExecutor
        = new ServerExecutor();

    public static PriorityExecutorService defaultPriorityExecutorService() {

        return defaultPriorityExecutorService.provide();

    }

    public static ExecutorService defaultExecutorService() {

        return defaultExecutorService.provide();

    }

    public static ExecutorService serverExecutor() {

        return serverExecutor.provide();

    }

}
