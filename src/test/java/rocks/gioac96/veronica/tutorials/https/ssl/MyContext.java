package rocks.gioac96.veronica.tutorials.https.ssl;

import rocks.gioac96.veronica.SSLContextFactory;

public class MyContext extends SSLContextFactory {

    @Override
    public void configure() {

        MyKeyStore myKeyStore = new MyKeyStore();

        password(myKeyStore.password());
        keyStore(myKeyStore);

    }

}
