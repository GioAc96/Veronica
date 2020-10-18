package rocks.gioac96.veronica.tutorials.https;

import rocks.gioac96.veronica.SecureServerBuilder;
import rocks.gioac96.veronica.tutorials.https.ssl.MyContext;

public class MyServer extends SecureServerBuilder {

    @Override
    public void configure() {

        port(443);
        sslContext(new MyContext());

    }

}
