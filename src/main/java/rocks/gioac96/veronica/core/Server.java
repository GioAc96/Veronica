package rocks.gioac96.veronica.core;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Http server initializer.
 */
@SuperBuilder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Server {

    private final int port;

    public HttpServer toHttpServer(HttpHandler httpHandler) throws IOException {

        HttpServer httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(port), 0);

        httpServer.createContext("/", httpHandler);

        httpServer.setExecutor(null);

        return httpServer;

    }

}
