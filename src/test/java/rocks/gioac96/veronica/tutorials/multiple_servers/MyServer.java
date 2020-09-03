package rocks.gioac96.veronica.tutorials.multiple_servers;

import rocks.gioac96.veronica.core.ServerFactory;

public class MyServer extends ServerFactory {

    @Override
    public void configure() {

        port(80);

    }

}
