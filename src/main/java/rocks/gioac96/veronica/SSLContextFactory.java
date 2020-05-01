package rocks.gioac96.veronica;

import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import rocks.gioac96.veronica.factories.ConfigurableFactory;
import rocks.gioac96.veronica.factories.CreationException;
import rocks.gioac96.veronica.factories.Factory;

/**
 * Factory for SSLContext.
 */
@SuppressWarnings({"checkstyle:AbbreviationAsWordInName"})
public abstract class SSLContextFactory implements ConfigurableFactory<SSLContext> {

    private KeyStore keyStore;

    private String password;

    protected void keyStore(KeyStore keyStore) {

        this.keyStore = keyStore;

    }

    protected void keyStore(Factory<KeyStore> keyStoreFactory) {

        keyStore(keyStoreFactory.build());

    }

    protected void password(String password) {

        this.password = password;

    }

    protected void password(Factory<String> passwordFactory) {

        password(passwordFactory.build());

    }

    private void validate() throws CreationException {

        if (keyStore == null) {
            throw new CreationException("KeyStore was not set", new NullPointerException());
        }

        if (password == null) {
            throw new CreationException("Password was not set", new NullPointerException());
        }

    }

    @Override
    public SSLContext build() throws CreationException {

        configure();

        validate();

        try {

            SSLContext sslContext = SSLContext.getInstance("TLS");

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, password.toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);

            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            return sslContext;

        } catch (Exception e) {

            throw new CreationException(e);

        }

    }

}
