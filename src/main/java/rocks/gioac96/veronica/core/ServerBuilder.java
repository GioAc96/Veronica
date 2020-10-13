package rocks.gioac96.veronica.core;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import lombok.NonNull;
import rocks.gioac96.veronica.common.CommonExecutorServices;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.providers.Provider;

public class ServerBuilder extends Builder<HttpServer> implements BuildsMultipleInstances {

    protected Integer port;
    protected Executor executor = CommonExecutorServices.serverExecutor();

    public ServerBuilder port(@NonNull Integer port) {

        this.port = port;
        return this;

    }

    public ServerBuilder port(@NonNull Provider<Integer> port) {

        return port(port.provide());

    }

    public ServerBuilder executor(@NonNull Executor executor) {

        this.executor = executor;
        return this;

    }

    public ServerBuilder executor(@NonNull Provider<Executor> executor) {

        return executor(executor.provide());

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
