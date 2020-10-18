package rocks.gioac96.veronica.core.tutorials.multiple_servers;

import rocks.gioac96.veronica.core.ServerBuilder;

public class MyServer extends ServerBuilder {

    @Override
    public void configure() {

        port(80);

    }

}
