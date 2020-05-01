package rocks.gioac96.veronica;

import javax.net.ssl.SSLContext;
import rocks.gioac96.veronica.factories.ConfigurableFactory;
import rocks.gioac96.veronica.factories.Factory;

/**
 * Secure server factory.
 */
public abstract class SecureServerFactory
    extends SecureServer.SecureServerBuilder<SecureServer, SecureServerFactory>
    implements ConfigurableFactory<SecureServer> {

    protected SecureServerFactory port(Factory<Integer> portFactory) {

        super.port(portFactory.build());

        return self();

    }

    protected SecureServerFactory sslContext(Factory<SSLContext> sslContextFactory) {

        super.sslContext(sslContextFactory.build());

        return self();

    }

    @Override
    protected SecureServerFactory self() {

        return this;

    }

    @Override
    public SecureServer build() {

        configure();

        return new SecureServer(this);

    }

}
