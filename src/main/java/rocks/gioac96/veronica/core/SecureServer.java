package rocks.gioac96.veronica.core;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * Https server initializer.
 */
@SuperBuilder
public final class SecureServer extends Server {

    @NonNull
    private final SSLContext sslContext;

    @Override
    public HttpServer toHttpServer(HttpHandler httpHandler) throws IOException {

        HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(getPort()), 0);

        httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {

            @Override
            public void configure(HttpsParameters params) {

                // initialise the SSL context
                SSLContext context = getSSLContext();
                SSLEngine engine = context.createSSLEngine();
                params.setNeedClientAuth(false);
                params.setCipherSuites(engine.getEnabledCipherSuites());
                params.setProtocols(engine.getEnabledProtocols());

                // Set the SSL parameters
                SSLParameters sslParameters = context.getSupportedSSLParameters();
                params.setSSLParameters(sslParameters);

            }
        });

        httpsServer.createContext("/", httpHandler);

        httpsServer.setExecutor(null);

        return httpsServer;

    }


}
