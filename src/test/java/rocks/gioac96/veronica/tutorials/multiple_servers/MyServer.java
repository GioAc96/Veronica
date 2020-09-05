package rocks.gioac96.veronica.tutorials.multiple_servers;

import rocks.gioac96.veronica.core.Server;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;

public class MyServer extends Server.ServerBuilder implements BuildsSingleInstance {

    @Override
    public void configure() {

        port(80);

    }

}
