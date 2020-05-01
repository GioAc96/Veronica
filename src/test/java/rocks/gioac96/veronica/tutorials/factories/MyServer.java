package rocks.gioac96.veronica.tutorials.factories;

import rocks.gioac96.veronica.ServerFactory;

public class MyServer extends ServerFactory {

    @Override
    public void configure() {

        port(8000);

    }

}
