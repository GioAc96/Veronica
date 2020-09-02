package rocks.gioac96.veronica.routing;

import lombok.NonNull;
import rocks.gioac96.veronica.factories.ConfigurableFactory;
import rocks.gioac96.veronica.factories.CreationException;
import rocks.gioac96.veronica.factories.Factory;

/**
 * Router factory.
 *
 * @param  Request type
 * @param  Response type
 */
public abstract class RouterFactory extends Router.RouterBuilder<
    Router,
    RouterFactory
    > implements ConfigurableFactory<Router> {

    @NonNull
    private Route fallbackRoute;

    protected RouterFactory fallbackRoute(@NonNull Factory<Route> fallbackRouteFactory)
        throws CreationException {

        return super.fallbackRoute(fallbackRouteFactory.build());

    }

    protected RouterFactory route(@NonNull Factory<Route> routeFactory) throws CreationException {

        return super.route(routeFactory.build());

    }

    protected RouterFactory route(@NonNull Factory<Route> routeFactory, Integer priority)
        throws CreationException {

        return super.route(routeFactory.build(), priority);

    }


    @Override
    protected RouterFactory self() {

        return this;

    }

    @Override
    public Router build() {

        configure();

        return new Router(this);

    }


}
