package rocks.gioac96.veronica.core;

import java.util.concurrent.ThreadPoolExecutor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import rocks.gioac96.veronica.common.CommonRequestMatchers;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
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
    @NonNull
    private Pipeline pipeline;

    protected ThreadPoolExecutor threadPool;

    protected Route(RouteBuilder b) {

        this.requestMatcher = b.requestMatcher;
        this.requestHandler = b.requestHandler;

        this.pipeline = b.pipeline;

    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static RouteBuilder builder() {

        class RouterBuilderImpl extends RouteBuilder implements BuildsMultipleInstances{

        }

        return new RouterBuilderImpl();

    }

    /**
     * Instantiates a builder for static server routes.
     * @param <P> the type of the file permissions
     * @return the instantiated builder
     */
    public static <P> StaticRouteBuilder<P> staticRouteBuilder() {

        return new StaticRouteBuilder<>();

    }

    public void useThreadPool(ThreadPoolExecutor threadPool) {

        this.threadPool = threadPool;

        this.pipeline.useThreadPool(threadPool);

    }

    /**
     * Checks whether the route should handle the specified {@link Request}.
     *
     * @param request request to handle
     * @return true iff the route should handle the specified {@link Request}
     */
    boolean shouldHandle(@NonNull Request request) {

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
    public abstract static class RouteBuilder extends Builder<Route> {

        private RequestMatcher requestMatcher = CommonRequestMatchers.neverMatch();

        private RequestHandler requestHandler;

        private Pipeline pipeline = Pipeline.builder().build();

        public RouteBuilder requestMatcher(@NonNull RequestMatcher requestMatcher) {

            this.requestMatcher = requestMatcher;
            return this;

        }
        
        protected RouteBuilder requestMatcher(@NonNull Provider<RequestMatcher> requestMatcherProvider)
            throws CreationException {

            return requestMatcher(requestMatcherProvider.provide());

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
        protected boolean isValid() {

            return isNotNull(
                requestMatcher,
                requestHandler,
                pipeline
            );

        }

        @Override
        protected Route instantiate() {

            return new Route(this);

        }

    }
    
}
