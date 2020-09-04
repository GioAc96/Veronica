package rocks.gioac96.veronica.core;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.Provider;

/**
 * Http server initializer.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Server {

    private final int port;

    protected Server(ServerBuilder b) {

        this.port = b.port;

    }

    public static ServerBuilder builder() {
        return new ServerBuilder();
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

        httpServer.setExecutor(null);

        return httpServer;

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public static class ServerBuilder extends Builder<Server> {

        private int port;

        public ServerBuilder port(int port) {

            this.port = port;
            return this;

        }

        public ServerBuilder port(@NonNull Provider<Integer> port) {

            return port(port.provide());

        }


        @Override
        protected Server instantiate() {

            return new Server(this);

        }

    }

}
