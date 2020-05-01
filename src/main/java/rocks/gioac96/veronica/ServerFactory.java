package rocks.gioac96.veronica;

import rocks.gioac96.veronica.factories.ConfigurableFactory;
import rocks.gioac96.veronica.factories.Factory;

/**
 * Server factory.
 */
public abstract class ServerFactory
    extends Server.ServerBuilder<Server, ServerFactory>
    implements ConfigurableFactory<Server> {

    protected ServerFactory port(Factory<Integer> portFactory) {

        super.port(portFactory.build());
        return self();

    }

    @Override
    protected ServerFactory self() {

        return this;

    }

    @Override
    public Server build() {

        configure();

        return new Server(this);

    }

}
