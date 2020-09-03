package rocks.gioac96.veronica.routing;

import java.util.concurrent.ThreadPoolExecutor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.matching.CommonRequestMatchers;
import rocks.gioac96.veronica.routing.matching.RequestMatcher;
import rocks.gioac96.veronica.routing.pipeline.Pipeline;
import rocks.gioac96.veronica.routing.pipeline.stages.RequestHandler;
import rocks.gioac96.veronica.statics.StaticRouteBuilder;

/**
 * Application route.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Route {

    @Getter
    @Setter
    @NonNull
    private RequestMatcher requestMatcher;

    @Getter
    @Setter
    @NonNull
    private RequestHandler requestHandler;

    @Getter
    @NonNull
    private Pipeline pipeline;

    protected ThreadPoolExecutor threadPool;

    protected Route(RouteBuilder<?, ?> b) {

        this.requestMatcher = b.requestMatcher;
        this.requestHandler = b.requestHandler;

        setPipeline(b.pipeline);

    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static RouteBuilder<?, ?> builder() {

        return new RouteBuilderImpl();

    }

    /**
     * Instantiates a builder for static server routes.
     * @param <P> the type of the file permissions
     * @return the instantiated builder
     */
    public static <P> StaticRouteBuilder<P> staticRouteBuilder() {

        return new StaticRouteBuilder<>();

    }

    /**
     * Sets the thread pool.
     * @param threadPool thread pool value
     */
    public void setThreadPool(ThreadPoolExecutor threadPool) {

        this.threadPool = threadPool;

        this.pipeline.setThreadPool(threadPool);

    }

    /**
     * Sets the pipeline of the route.
     * @param pipeline the pipeline
     */
    public void setPipeline(Pipeline pipeline) {

        this.pipeline = pipeline;

        pipeline.setThreadPool(this.threadPool);

    }

    /**
     * Checks whether the route should handle the specified {@link Request}.
     *
     * @param request request to handle
     * @return true iff the route should handle the specified {@link Request}
     */
    public boolean shouldHandle(@NonNull Request request) {

        return requestMatcher.matches(request);

    }

    /**
     * Handles the specified {@link Request} via passing it through the route's {@link Pipeline}.
     *
     * @param request request to handle
     * @return the generated {@link Response}
     */
    public Response handle(@NonNull Request request) {

        return pipeline.handle(request, requestHandler);

    }

    @SuppressWarnings({"checkstyle:ModifierOrder", "checkstyle:MissingJavadocType", "checkstyle:MissingJavadocMethod"})
    public static abstract class RouteBuilder<
        C extends Route,
        B extends RouteBuilder<C, B>
        > {

        @NonNull
        private RequestMatcher requestMatcher = CommonRequestMatchers.neverMatch();

        @NonNull
        private RequestHandler requestHandler;

        @NonNull
        private Pipeline pipeline = Pipeline.builder().build();

        public B requestMatcher(@NonNull RequestMatcher requestMatcher) {

            this.requestMatcher = requestMatcher;
            return self();

        }

        public B alwaysMatch() {

            return requestMatcher(CommonRequestMatchers.alwaysMatch());

        }

        public B requestHandler(@NonNull RequestHandler requestHandler) {

            this.requestHandler = requestHandler;
            return self();

        }

        public B handler(@NonNull RequestHandler requestHandler) {

            return requestHandler(requestHandler);

        }

        public B pipeline(@NonNull Pipeline pipeline) {

            this.pipeline = pipeline;
            return self();

        }

        protected abstract B self();

        public abstract C build();

    }

    private static final class RouteBuilderImpl extends RouteBuilder<
        Route,
        RouteBuilderImpl
        > {

        private RouteBuilderImpl() {
        }

        protected Route.RouteBuilderImpl self() {

            return this;

        }

        public Route build() {

            return new Route(this);

        }

    }

}
