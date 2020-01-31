package org.gioac96.veronica.routing;

import lombok.Getter;
import lombok.NonNull;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.util.PriorityList;

public class Router {

    @NonNull
    @Getter
    protected final PriorityList<Route> routes;

    @Getter
    @NonNull
    protected final Route fallbackRoute;

    public Router(
        @NonNull Route fallbackRoute
    ) {

        this.routes = new PriorityList<>();
        this.fallbackRoute = fallbackRoute;

    }

    public Response route(Request request) {

        for (Route route : routes) {

            if (route.shouldHandle(request)) {

                return route.handle(request);

            }

        }

        return fallbackRoute.handle(request);

    }

}
