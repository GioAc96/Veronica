package rocks.gioac96.veronica.core;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import lombok.NonNull;
import rocks.gioac96.veronica.core.providers.CreationException;
import rocks.gioac96.veronica.core.providers.Provider;

public class SecureServerBuilder extends ServerBuilder {

    protected SSLContext sslContext;

    public SecureServerBuilder sslContext(@NonNull SSLContext sslContext) {

        this.sslContext = sslContext;
        return this;

    }

    protected SecureServerBuilder sslContext(@NonNull Provider<SSLContext> sslContextProvider) {

        return sslContext(sslContextProvider.provide());

    }

    @Override
    protected boolean isValid() {

        return super.isValid()
            && sslContext != null;

    }

    @Override
    protected HttpsServer instantiate() {

        HttpsServer httpsServer;
        try {

            httpsServer = HttpsServer.create(new InetSocketAddress(super.port), 0);

        } catch (IOException e) {

            throw new CreationException(e);

        }

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


        httpsServer.setExecutor(super.executor);

        return httpsServer;

    }

}
