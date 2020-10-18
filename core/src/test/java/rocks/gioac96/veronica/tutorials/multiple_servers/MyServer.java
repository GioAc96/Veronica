package rocks.gioac96.veronica.tutorials.multiple_servers;

import rocks.gioac96.veronica.ServerBuilder;

public class MyServer extends ServerBuilder {

    @Override
    public void configure() {

        port(80);

    }

}
