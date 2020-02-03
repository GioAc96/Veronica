package org.gioac96.veronica.routing;

import lombok.Getter;
import lombok.NonNull;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.util.PrioritySet;

/**
 * Application router, responsible for routing {@link Request} to a specific route, that handles the
 * request and generates a response.
 */
public class Router {

    @NonNull
    @Getter
    protected final PrioritySet<Route> routes;

    @Getter
    @NonNull
    protected final Route fallbackRoute;

    public Router(
        @NonNull Route fallbackRoute
    ) {

        this.routes = new PrioritySet<>();
        this.fallbackRoute = fallbackRoute;

    }

    /**
     * Routes the specified {@link Request}.
     * @param request request to route
     * @return generated {@link Response}
     */
    public Response route(Request request) {

        for (Route route : routes) {

            if (route.shouldHandle(request)) {

                return route.handle(request);

            }

        }

        return fallbackRoute.handle(request);

    }

}
