package rocks.gioac96.veronica.routing;

import java.util.Collection;
import java.util.Collections;
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

    @Generated
    protected Router(RouterBuilder<Q, S, ?, ?> b) {
        this.fallbackRoute = b.fallbackRoute;
        this.routes = b.routes;
    }


    @Generated
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static <Q extends Request, S extends Response> RouterBuilder<Q, S, ?, ?> builder() {

        return new RouterBuilderImpl<Q, S>();

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

    @Generated
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public abstract static class RouterBuilder<
        Q extends Request,
        S extends Response,
        C extends Router<Q, S>,
        B extends RouterBuilder<Q, S, C, B>
        > {

        private @NonNull Route<Q, S> fallbackRoute;
        private @NonNull PrioritySet<Route<Q, S>> routes = new PrioritySet<>();

        public B fallbackRoute(@NonNull Route<Q, S> fallbackRoute) {

            this.fallbackRoute = fallbackRoute;
            return self();

        }

        public B routes(@NonNull Route<Q, S>... routes) {

            Collections.addAll(this.routes, routes);

            return self();

        }

        public B routes(@NonNull Collection<Route<Q, S>> routes) {

            this.routes.addAll(routes);

            return self();

        }

        protected abstract B self();

        public abstract C build();

        public String toString() {

            return
                "Router.RouterBuilder(fallbackRoute=" + this.fallbackRoute
                    + ", routes=" + this.routes
                    + ")";

        }

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

        public Router<Q, S> build() {

            return new Router<Q, S>(this);

        }

    }

}
