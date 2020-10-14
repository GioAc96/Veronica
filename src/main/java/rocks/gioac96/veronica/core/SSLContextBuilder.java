package rocks.gioac96.veronica.core;

import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import lombok.NonNull;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.providers.Provider;

/**
 * Factory for SSLContext.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public abstract class SSLContextBuilder extends Builder<SSLContext> {

    private KeyStore keyStore;
    private String password;

    protected SSLContextBuilder keyStore(@NonNull KeyStore keyStore) {

        this.keyStore = keyStore;
        return this;

    }

    protected SSLContextBuilder keyStore(@NonNull Provider<KeyStore> keyStoreProvider) {

        return keyStore(keyStoreProvider.provide());

    }

    protected SSLContextBuilder password(@NonNull String password) {

        this.password = password;
        return this;

    }

    protected SSLContextBuilder password(@NonNull Provider<String> passwordProvider) {

        return password(passwordProvider.provide());

    }

    @Override
    protected boolean isValid() {

        return super.isValid()
            && keyStore != null
            && password != null;

    }

    @Override
    public SSLContext instantiate() throws CreationException {

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
