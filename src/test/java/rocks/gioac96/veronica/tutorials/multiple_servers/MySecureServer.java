package rocks.gioac96.veronica.tutorials.multiple_servers;

import rocks.gioac96.veronica.core.SecureServer;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;
import rocks.gioac96.veronica.tutorials.https.ssl.MyContext;

public class MySecureServer
    extends SecureServer.SecureServerBuilder
    implements BuildsSingleInstance {

    @Override
    public void configure() {

        port(443);
        sslContext(new MyContext());

    }
}
