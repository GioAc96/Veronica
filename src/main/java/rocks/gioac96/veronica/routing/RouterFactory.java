package rocks.gioac96.veronica.routing;

import lombok.NonNull;
import rocks.gioac96.veronica.factories.CreationException;
import rocks.gioac96.veronica.factories.Factory;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;

/**
 * Router factory.
 *
 * @param <Q> Request type
 * @param <S> Response type
 */
public abstract class RouterFactory<
    Q extends Request,
    S extends Response
    > extends Router.RouterBuilder<
    Q,
    S,
    Router<Q, S>,
    RouterFactory<Q, S>
    > implements Factory<Router<Q, S>> {

    @NonNull
    private Route<Q, S> fallbackRoute;

    protected RouterFactory<Q, S> fallbackRoute(@NonNull Factory<Route<Q, S>> fallbackRouteFactory)
        throws CreationException {

        return super.fallbackRoute(fallbackRouteFactory.build());

    }

    protected RouterFactory<Q, S> route(@NonNull Factory<Route<Q, S>> routeFactory) throws CreationException {

        return super.route(routeFactory.build());

    }

    protected RouterFactory<Q, S> route(@NonNull Factory<Route<Q, S>> routeFactory, Integer priority)
        throws CreationException {

        return super.route(routeFactory.build(), priority);

    }


    @Override
    protected RouterFactory<Q, S> self() {

        return this;

    }

    @Override
    public Router<Q, S> build() {

        configure();

        return new Router<>(this);

    }


}
