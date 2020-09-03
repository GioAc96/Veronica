package rocks.gioac96.veronica.core;

import java.util.Collection;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.util.PrioritySet;

/**
 * Application router, responsible for routing {@link Request} to a specific {@link Route}, that handles the
 * request and generates a response.
 * Builder is extensible with lombok's {@link lombok.experimental.SuperBuilder}.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class Router {

    @Getter
    @Setter
    @NonNull
    protected Route fallbackRoute;

    @Getter
    @Setter
    @NonNull
    protected PrioritySet<Route> routes;

    protected ThreadPoolExecutor threadPool;

    @Generated
    protected Router(RouterBuilder<?, ?> b) {

        this.fallbackRoute = b.fallbackRoute;
        this.routes = b.routes;

        for (Route route : routes) {

            route.setThreadPool(threadPool);

        }

    }


    @Generated
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static  RouterBuilder<?, ?> builder() {

        return new RouterBuilderImpl();

    }

    /**
     * Sets the thread pool.
     * @param threadPool the thread pool
     */
    public void setThreadPool(ThreadPoolExecutor threadPool) {

        this.threadPool = threadPool;

        for (Route route : routes) {

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
    public Response route(Request request) {

        return routes.firstOrDefault(
            route -> route.shouldHandle(request),
            fallbackRoute
        ).handle(request);

    }

    /**
     * Adds a route to the router.
     * @param route the route to add
     */
    public void addRoute(Route route) {

        route.setThreadPool(this.threadPool);

        this.routes.add(route);

    }

    /**
     * Adds a route to the router, with the specified priority.
     * @param route the route to add
     * @param priority the priority of the route
     */
    public void addRoute(Route route, int priority) {

        route.setThreadPool(this.threadPool);

        this.routes.add(route, priority);

    }

    @Generated
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public abstract static class RouterBuilder<
        C extends Router,
        B extends RouterBuilder<C, B>
        > {

        @NonNull
        private final PrioritySet<Route> routes = new PrioritySet<>();

        @NonNull
        private Route fallbackRoute;

        public B fallbackRoute(@NonNull Route fallbackRoute) {

            this.fallbackRoute = fallbackRoute;

            return self();

        }

        public B route(@NonNull Route route) {

            this.routes.add(route);

            return self();

        }

        public B route(@NonNull Route route, Integer priority) {

            this.routes.add(route, priority);

            return self();

        }


        @SuppressWarnings("unused")
        public B routes(Collection<Route> routes) {

            this.routes.addAll(routes);

            return self();

        }

        @SuppressWarnings("unused")
        public B routes(PrioritySet<Route> routes) {

            this.routes.addAll(routes);

            return self();

        }

        protected abstract B self();

        public abstract C build();

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class RouterBuilderImpl extends RouterBuilder<
        Router,
        RouterBuilderImpl
        > {

        protected Router.RouterBuilderImpl self() {

            return this;

        }

        @SuppressWarnings("unused")
        public Router build() {

            return new Router(this);

        }

    }

}
