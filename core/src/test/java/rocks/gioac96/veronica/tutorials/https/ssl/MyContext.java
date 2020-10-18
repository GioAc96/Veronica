package rocks.gioac96.veronica.tutorials.https.ssl;

import rocks.gioac96.veronica.SSLContextBuilder;

public class MyContext extends SSLContextBuilder  {

    @Override
    public void configure() {

        MyKeyStore myKeyStore = new MyKeyStore();

        password(myKeyStore.password());
        keyStore(myKeyStore);

    }

}
