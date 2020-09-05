package rocks.gioac96.veronica.core;

import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.common.CommonRoutes;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.providers.DeclaresPriority;
import rocks.gioac96.veronica.providers.Provider;
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
    protected Route defaultRoute;

    @Getter
    @Setter
    @NonNull
    protected PrioritySet<Route> routes;

    protected ThreadPoolExecutor threadPool;

    protected Router(RouterBuilder b) {

        this.defaultRoute = b.defaultRoute;
        this.routes = b.routes;

        for (Route route : routes) {

            route.useThreadPool(threadPool);

        }

    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static RouterBuilder builder() {

        class RouterBuilderImpl extends RouterBuilder implements BuildsMultipleInstances {

        }

        return new RouterBuilderImpl();

    }

    /**
     * Sets the thread pool.
     * @param threadPool the thread pool
     */
    void useThreadPool(ThreadPoolExecutor threadPool) {

        this.threadPool = threadPool;

        for (Route route : routes) {

            route.useThreadPool(threadPool);

        }

    }


    Response route(Request request) {

        return routes.firstOrDefault(
            route -> route.shouldHandle(request),
            defaultRoute
        ).handle(request);

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public abstract static class RouterBuilder extends Builder<Router> {

        private final PrioritySet<Route> routes = new PrioritySet<>();

        private Route defaultRoute = CommonRoutes.notFound();

        public RouterBuilder defaultRoute(@NonNull Route defaultRoute) {

            this.defaultRoute = defaultRoute;

            return this;

        }

        public RouterBuilder defaultRoute(@NonNull Provider<Route> defaultRouteProvider)
            throws CreationException {

            return defaultRoute(defaultRouteProvider.provide());

        }

        public RouterBuilder route(@NonNull Route route) {

            this.routes.add(route);

            return this;

        }

        public RouterBuilder route(@NonNull Route route, Integer priority) {

            this.routes.add(route, priority);

            return this;

        }

        public RouterBuilder route(@NonNull Provider<Route> routeProvider) throws CreationException {

            if (routeProvider instanceof DeclaresPriority) {

                return route(routeProvider.provide(), ((DeclaresPriority) routeProvider).priority());

            } else {

                return route(routeProvider.provide());

            }

        }

        @Override
        protected boolean isValid() {

            return routes.stream().noneMatch(Objects::isNull) && isNotNull(
                defaultRoute
            );

        }

        @Override
        protected Router instantiate() {

            return new Router(this);

        }

    }

}
