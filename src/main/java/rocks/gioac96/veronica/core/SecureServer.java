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
import rocks.gioac96.veronica.providers.Provider;

/**
 * Https server initializer.
 */
public final class SecureServer extends Server {

    @NonNull
    private final SSLContext sslContext;

    protected SecureServer(SecureServerBuilder b) {
        super(b);
        this.sslContext = b.sslContext;
    }

    public static SecureServerBuilder builder() {
        return new SecureServerBuilder();
    }

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

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public static class SecureServerBuilder extends ServerBuilder {

        private @NonNull SSLContext sslContext;

        public SecureServerBuilder sslContext(@NonNull SSLContext sslContext) {

            this.sslContext = sslContext;
            return this;

        }

        protected SecureServerBuilder sslContext(@NonNull Provider<SSLContext> sslContextProvider) {

            return sslContext(sslContextProvider.provide());

        }

        @Override
        protected Server instantiate() {

            return new SecureServer(this);

        }

    }

}
