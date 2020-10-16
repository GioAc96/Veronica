package rocks.gioac96.veronica.core;

import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.providers.ConfigurableProvider;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.static_server.StaticRouteBuilder;

public class Route {

    @Getter
    private final RequestMatcher requestMatcher;

    @Getter
    private final RequestHandler requestHandler;

    protected Route(RouteBuilder b) {

        this.requestMatcher = b.requestMatcher;
        this.requestHandler = b.requestHandler;

    }

    public static RouteBuilder builder() {

        return new RouteBuilder();

    }

    public static <P> StaticRouteBuilder<P> staticRouteBuilder() {

        return new StaticRouteBuilder<P>();

    }

    public static class RouteBuilder extends ConfigurableProvider<Route> {

        private RequestMatcher requestMatcher;
        private RequestHandler requestHandler;

        public RouteBuilder requestMatcher(@NonNull RequestMatcher requestMatcher) {

            this.requestMatcher = requestMatcher;
            return this;

        }

        public RouteBuilder requestMatcher(@NonNull Provider<RequestMatcher> requestMatcher) {

            return requestMatcher(requestMatcher.provide());

        }

        public RouteBuilder requestHandler(@NonNull RequestHandler requestHandler) {

            this.requestHandler = requestHandler;
            return this;

        }

        public RouteBuilder requestHandler(@NonNull Provider<RequestHandler> requestHandler) {

            return requestHandler(requestHandler.provide());

        }

        @Override
        protected boolean isValid() {

            return requestMatcher != null
                && requestHandler != null;

        }

        @Override
        protected Route instantiate() {

            return new Route(this);

        }

    }

}
