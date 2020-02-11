package org.gioac96.veronica.routing;

import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.util.PrioritySet;

/**
 * Application router, responsible for routing {@link Request} to a specific {@link Route}, that handles the
 * request and generates a response.
 * Builder is extensible with lombok's {@link lombok.experimental.SuperBuilder}.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Router {

    @Getter
    @Setter
    @NonNull
    protected Route fallbackRoute;

    @Getter
    @Setter
    @NonNull
    protected PrioritySet<Route> routes;

    protected Router(RouterBuilder<?, ?> b) {

        this.fallbackRoute = b.fallbackRoute;
        this.routes = b.routes;

    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static RouterBuilder<?, ?> builder() {

        return new RouterBuilderImpl();

    }

    /**
     * Routes the specified {@link Request}. If none of the available routes can handle the request, the request is
     * handled by the fallback route.
     *
     * @param request request to route
     * @return generated {@link Response}
     */
    public Response route(Request request) {

        return routes.firstOrDefault(
            route -> route.shouldHandle(request),
            fallbackRoute
        ).handle(request);

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public abstract static class RouterBuilder<C extends Router, B extends RouterBuilder<C, B>> {

        private @NonNull Route fallbackRoute;
        private @NonNull PrioritySet<Route> routes = new PrioritySet<>();

        public B fallbackRoute(@NonNull Route fallbackRoute) {

            this.fallbackRoute = fallbackRoute;
            return self();

        }

        public RouterBuilder routes(Route... routes) {

            Collections.addAll(this.routes, routes);

            return self();

        }

        public RouterBuilder routes(Collection<Route> routes) {

            this.routes.addAll(routes);

            return self();

        }

        protected abstract B self();

        public abstract C build();

        public String toString() {

            return "Router.RouterBuilder(fallbackRoute=" + this.fallbackRoute + ", routes=" + this.routes + ")";

        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class RouterBuilderImpl extends RouterBuilder<Router, RouterBuilderImpl> {

        protected Router.RouterBuilderImpl self() {

            return this;

        }

        public Router build() {

            return new Router(this);

        }

    }

}
