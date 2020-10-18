package rocks.gioac96.veronica.core;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import lombok.NonNull;
import rocks.gioac96.veronica.core.common.CommonExecutorServices;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.CreationException;
import rocks.gioac96.veronica.core.providers.Provider;

public class ServerBuilder extends ConfigurableProvider<HttpServer> {

    protected Integer port;
    protected Executor executor = CommonExecutorServices.serverExecutor();

    public ServerBuilder port(@NonNull Integer port) {

        this.port = port;
        return this;

    }

    public ServerBuilder port(@NonNull Provider<Integer> portProvider) {

        return port(portProvider.provide());

    }

    public ServerBuilder executor(@NonNull Executor executor) {

        this.executor = executor;
        return this;

    }

    public ServerBuilder executor(@NonNull Provider<Executor> executorProvider) {

        return executor(executorProvider.provide());

    }

    @Override
    protected boolean isValid() {

        return super.isValid()
            && port != null
            && port >= 0
            && port <= 65535;

    }

    @Override
    protected HttpServer instantiate() {

        try {

            HttpServer httpServer = HttpServer.create();

            httpServer.bind(new InetSocketAddress(port), 0);

            httpServer.setExecutor(executor);

            return httpServer;

        } catch (IOException e) {

            throw new CreationException(e);

        }

    }

}
