package rocks.gioac96.veronica.core.tutorials.multiple_servers;

import rocks.gioac96.veronica.core.SecureServerBuilder;
import rocks.gioac96.veronica.core.tutorials.https.ssl.MyContext;

public class MySecureServer extends SecureServerBuilder {

    @Override
    public void configure() {

        port(443);
        sslContext(new MyContext());

    }
}
