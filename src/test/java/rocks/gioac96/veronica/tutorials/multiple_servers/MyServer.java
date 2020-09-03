package rocks.gioac96.veronica.tutorials.multiple_servers;

import rocks.gioac96.veronica.core.Server;

public class MyServer extends Server.ServerBuilder {

    @Override
    public void configure() {

        port(80);

    }

}
