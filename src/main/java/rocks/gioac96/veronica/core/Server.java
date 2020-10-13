package rocks.gioac96.veronica.core;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.common.CommonExecutorServices;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.Provider;

/**
 * Http server initializer.
 */
@Getter
public class Server {

    private final int port;
    private final Executor executor;

    protected Server(ServerBuilder b) {

        this.port = b.port;
        this.executor = b.executor;

    }

    public static ServerBuilder builder() {

        class ServerBuilderImpl extends ServerBuilder implements BuildsMultipleInstances {

        }

        return new ServerBuilderImpl();

    }

    /**
     * Instantiates an http server.
     * @param httpHandler the handler of the server
     * @return the instantiated http server
     * @throws IOException on failure to bind port
     */
    public HttpServer toHttpServer(HttpHandler httpHandler) throws IOException {

        HttpServer httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(port), 0);

        httpServer.createContext("/", httpHandler);

        httpServer.setExecutor(executor);

        return httpServer;

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public abstract static class ServerBuilder extends Builder<Server> {

        private Integer port;
        private Executor executor = CommonExecutorServices.serverExecutor();

        public ServerBuilder port(int port) {

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
        protected Server instantiate() {

            return new Server(this);

        }

    }

}
