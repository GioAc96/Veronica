package org.gioac96.veronica.routing;

import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
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
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Router {

    @Getter
    @NonNull
    protected PrioritySet<Route> routes = new PrioritySet<>();

    @Getter
    @NonNull
    protected final Route fallbackRoute;

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

    public static RouterBuilder builder() {

        return new RouterBuilder();

    }

    public static class RouterBuilder {

        private PrioritySet<Route> routes = new PrioritySet<>();

        private Route fallbackRoute;

        public RouterBuilder routes(Route... routes) {

            Collections.addAll(this.routes, routes);

            return this;

        }

        public RouterBuilder routes(Collection<Route> routes) {

            this.routes.addAll(routes);

            return this;

        }

        public RouterBuilder fallbackRoute(Route fallbackRoute) {

            this.fallbackRoute = fallbackRoute;

            return this;

        }

        public Router build() {

            return new Router(routes, fallbackRoute);

        }

    }

}
