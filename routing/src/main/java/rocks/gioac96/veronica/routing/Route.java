package rocks.gioac96.veronica.routing;

import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.Provider;

@Getter
public class Route {

    private final RequestMatcher requestMatcher;
    private final RequestHandler requestHandler;

    protected Route(RouteBuilder b) {

        this.requestMatcher = b.requestMatcher;
        this.requestHandler = b.requestHandler;

    }

    public static RouteBuilder builder() {

        return new RouteBuilder();

    }

    public static class RouteBuilder extends ConfigurableProvider<Route> {

        protected RequestMatcher requestMatcher;
        protected RequestHandler requestHandler;

        public RouteBuilder requestMatcher(@NonNull RequestMatcher requestMatcher) {

            this.requestMatcher = requestMatcher;
            return this;

        }

        public RouteBuilder requestMatcher(@NonNull Provider<RequestMatcher> requestMatcherProvider) {

            return requestMatcher(requestMatcherProvider.provide());

        }

        public RouteBuilder requestHandler(@NonNull RequestHandler requestHandler) {

            this.requestHandler = requestHandler;
            return this;

        }

        public RouteBuilder requestHandler(@NonNull Provider<RequestHandler> requestHandlerProvider) {

            return requestHandler(requestHandlerProvider.provide());

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
