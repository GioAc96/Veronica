package rocks.gioac96.veronica.validation.common;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import rocks.gioac96.veronica.validation.common.executor_services.DefaultExecutorService;
import rocks.gioac96.veronica.validation.common.executor_services.DefaultPriorityExecutorService;
import rocks.gioac96.veronica.validation.common.executor_services.ServerExecutorService;
import rocks.gioac96.veronica.core.concurrency.PriorityExecutorService;
import rocks.gioac96.veronica.core.providers.Provider;

/**
 * Framework common executor services.
 */
public class CommonExecutorServices {

    private static final Provider<? extends PriorityExecutorService> defaultPriorityExecutorService
        = new DefaultPriorityExecutorService();

    private static final Provider<ExecutorService> defaultExecutorService
        = new DefaultExecutorService();

    private static final Provider<ExecutorService> serverExecutor
        = new ServerExecutorService();

    /**
     * Gets the framework's default priority executor service.
     *
     * @return the framework's default priority executor service.
     */
    public static PriorityExecutorService defaultPriorityExecutorService() {

        return defaultPriorityExecutorService.provide();

    }

    /**
     * Gets the framework's default executor service.
     *
     * @return the framework's default executor service.
     */
    public static ExecutorService defaultExecutorService() {

        return defaultExecutorService.provide();

    }

    /**
     * Gets the framework's default executor used by {@link com.sun.net.httpserver.HttpsServer} instances.
     *
     * @return the executor.
     */
    public static Executor serverExecutor() {

        return serverExecutor.provide();

    }

}
