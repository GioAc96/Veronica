package rocks.gioac96.veronica.routing;

import java.util.Collection;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.util.PrioritySet;

/**
 * Application router, responsible for routing {@link Request} to a specific {@link Route}, that handles the
 * request and generates a response.
 * Builder is extensible with lombok's {@link lombok.experimental.SuperBuilder}.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class Router<Q extends Request, S extends Response> {

    @Getter
    @Setter
    @NonNull
    protected Route<Q, S> fallbackRoute;

    @Getter
    @Setter
    @NonNull
    protected PrioritySet<Route<Q, S>> routes;

    protected ThreadPoolExecutor threadPool;

    @Generated
    protected Router(RouterBuilder<Q, S, ?, ?> b) {

        this.fallbackRoute = b.fallbackRoute;
        this.routes = b.routes;

        for (Route<Q, S> route : routes) {

            route.setThreadPool(threadPool);

        }

    }


    @Generated
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static <Q extends Request, S extends Response> RouterBuilder<Q, S, ?, ?> builder() {

        return new RouterBuilderImpl<>();

    }

    /**
     * Sets the thread pool.
     * @param threadPool the thread pool
     */
    public void setThreadPool(ThreadPoolExecutor threadPool) {

        this.threadPool = threadPool;

        for (Route<Q, S> route : routes) {

            route.setThreadPool(threadPool);

        }

    }

    /**
     * Routes the specified {@link Request}. If none of the available routes can handle the request, the request is
     * handled by the fallback route.
     *
     * @param request request to route
     * @return generated {@link Response}
     */
    public S route(Q request) {

        return routes.firstOrDefault(
            route -> route.shouldHandle(request),
            fallbackRoute
        ).handle(request);

    }

    /**
     * Adds a route to the router.
     * @param route the route to add
     */
    public void addRoute(Route<Q, S> route) {

        route.setThreadPool(this.threadPool);

        this.routes.add(route);

    }

    /**
     * Adds a route to the router, with the specified priority.
     * @param route the route to add
     * @param priority the priority of the route
     */
    public void addRoute(Route<Q, S> route, int priority) {

        route.setThreadPool(this.threadPool);

        this.routes.add(route, priority);

    }

    @Generated
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public abstract static class RouterBuilder<
        Q extends Request,
        S extends Response,
        C extends Router<Q, S>,
        B extends RouterBuilder<Q, S, C, B>
        > {

        @NonNull
        private final PrioritySet<Route<Q, S>> routes = new PrioritySet<>();

        @NonNull
        private Route<Q, S> fallbackRoute;

        public B fallbackRoute(@NonNull Route<Q, S> fallbackRoute) {

            this.fallbackRoute = fallbackRoute;

            return self();

        }

        public B route(@NonNull Route<Q, S> route) {

            this.routes.add(route);

            return self();

        }

        public B route(@NonNull Route<Q, S> route, Integer priority) {

            this.routes.add(route, priority);

            return self();

        }


        @SuppressWarnings("unused")
        public B routes(Collection<Route<Q, S>> routes) {

            this.routes.addAll(routes);

            return self();

        }

        @SuppressWarnings("unused")
        public B routes(PrioritySet<Route<Q, S>> routes) {

            this.routes.addAll(routes);

            return self();

        }

        protected abstract B self();

        public abstract C build();

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class RouterBuilderImpl<
        Q extends Request,
        S extends Response
        > extends RouterBuilder<
        Q,
        S,
        Router<Q, S>,
        RouterBuilderImpl<Q, S>
        > {

        protected Router.RouterBuilderImpl<Q, S> self() {

            return this;

        }

        @SuppressWarnings("unused")
        public Router<Q, S> build() {

            return new Router<>(this);

        }

    }

}
