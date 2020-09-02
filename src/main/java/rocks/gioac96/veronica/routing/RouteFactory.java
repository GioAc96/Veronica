package rocks.gioac96.veronica.routing;

import lombok.NonNull;
import rocks.gioac96.veronica.factories.ConfigurableFactory;
import rocks.gioac96.veronica.factories.CreationException;
import rocks.gioac96.veronica.factories.Factory;
import rocks.gioac96.veronica.factories.PriorityFactory;
import rocks.gioac96.veronica.routing.matching.RequestMatcher;
import rocks.gioac96.veronica.routing.pipeline.Pipeline;
import rocks.gioac96.veronica.routing.pipeline.stages.RequestHandler;

/**
 * Route factory.
 */
public abstract class RouteFactory extends Route.RouteBuilder<
    Route,
    RouteFactory
    > implements
    PriorityFactory<Route>,
    ConfigurableFactory<Route> {

    protected RouteFactory requestMatcher(@NonNull Factory<RequestMatcher> requestMatcherFactory)
        throws CreationException {

        return super.requestMatcher(requestMatcherFactory.build());

    }

    protected RouteFactory requestHandler(@NonNull Factory<RequestHandler> requestHandlerFactory)
        throws CreationException {

        return super.requestHandler(requestHandlerFactory.build());

    }

    protected RouteFactory pipeline(@NonNull Factory<Pipeline> pipelineFactory) throws CreationException {

        return super.pipeline(pipelineFactory.build());

    }

    @Override
    protected RouteFactory self() {

        return this;

    }

    @Override
    public Route build() {

        configure();

        return new Route(this);

    }
}
