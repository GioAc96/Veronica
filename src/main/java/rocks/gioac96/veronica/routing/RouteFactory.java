package rocks.gioac96.veronica.routing;

import lombok.NonNull;
import rocks.gioac96.veronica.factories.ConfigurableFactory;
import rocks.gioac96.veronica.factories.CreationException;
import rocks.gioac96.veronica.factories.Factory;
import rocks.gioac96.veronica.factories.PriorityFactory;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.matching.RequestMatcher;
import rocks.gioac96.veronica.routing.pipeline.Pipeline;
import rocks.gioac96.veronica.routing.pipeline.stages.RequestHandler;

/**
 * Route factory.
 *
 * @param <Q> Request type
 * @param <S> Response type
 */
public abstract class RouteFactory<
    Q extends Request,
    S extends Response
    > extends Route.RouteBuilder<
    Q,
    S,
    Route<Q, S>,
    RouteFactory<Q, S>
    > implements
    PriorityFactory<Route<Q, S>>,
    ConfigurableFactory<Route<Q, S>> {

    protected RouteFactory<Q, S> requestMatcher(@NonNull Factory<RequestMatcher<Q>> requestMatcherFactory)
        throws CreationException {

        return super.requestMatcher(requestMatcherFactory.build());

    }

    protected RouteFactory<Q, S> requestHandler(@NonNull Factory<RequestHandler<Q, S>> requestHandlerFactory)
        throws CreationException {

        return super.requestHandler(requestHandlerFactory.build());

    }

    protected RouteFactory<Q, S> pipeline(@NonNull Factory<Pipeline<Q, S>> pipelineFactory) throws CreationException {

        return super.pipeline(pipelineFactory.build());

    }

    @Override
    protected RouteFactory<Q, S> self() {

        return this;

    }

    @Override
    public Route<Q, S> build() {

        configure();

        return new Route<>(this);

    }
}
