package rocks.gioac96.veronica.tutorials.https.ssl;

import rocks.gioac96.veronica.core.SSLContextBuilder;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;

public class MyContext extends SSLContextBuilder implements BuildsSingleInstance {

    @Override
    public void configure() {

        MyKeyStore myKeyStore = new MyKeyStore();

        password(myKeyStore.password());
        keyStore(myKeyStore);

    }

}
