package rocks.gioac96.veronica.core;

import java.util.concurrent.ThreadPoolExecutor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import rocks.gioac96.veronica.common.CommonRequestMatchers;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.static_server.StaticRouteBuilder;

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
    @Setter
    @NonNull
    private Pipeline pipeline;

    protected ThreadPoolExecutor threadPool;

    protected Route(RouteBuilder b) {

        this.requestMatcher = b.requestMatcher;
        this.requestHandler = b.requestHandler;

        setPipeline(b.pipeline);

    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static RouteBuilder builder() {

        return new RouteBuilder();

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

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public static class RouteBuilder extends Builder<Route> {

        @NonNull
        private RequestMatcher requestMatcher = CommonRequestMatchers.neverMatch();

        @NonNull
        private RequestHandler requestHandler;

        @NonNull
        private Pipeline pipeline = Pipeline.builder().build();

        public RouteBuilder requestMatcher(@NonNull RequestMatcher requestMatcher) {

            this.requestMatcher = requestMatcher;
            return this;

        }
        
        protected RouteBuilder requestMatcher(@NonNull Provider<RequestMatcher> requestMatcherProvider)
            throws CreationException {

            return requestMatcher(requestMatcherProvider.provide());

        }


        public RouteBuilder alwaysMatch() {

            return requestMatcher(CommonRequestMatchers.alwaysMatch());

        }

        public RouteBuilder requestHandler(@NonNull RequestHandler requestHandler) {

            this.requestHandler = requestHandler;
            return this;

        }

        protected RouteBuilder requestHandler(@NonNull Provider<RequestHandler> requestHandlerProvider)
            throws CreationException {

            return requestHandler(requestHandlerProvider.provide());

        }

        public RouteBuilder pipeline(@NonNull Pipeline pipeline) {

            this.pipeline = pipeline;
            return this;

        }

        protected RouteBuilder pipeline(@NonNull Provider<Pipeline> pipelineProvider) throws CreationException {

            return pipeline(pipelineProvider.provide());

        }


        @Override
        protected Route instantiate() {

            return new Route(this);

        }

    }
    
}
