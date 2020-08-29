package rocks.gioac96.veronica.routing;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;
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
import rocks.gioac96.veronica.routing.pipeline.stages.RequestHandlerPayload;

/**
 * Application route.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Route<Q extends Request, S extends Response> {

    @Getter
    @Setter
    @NonNull
    private RequestMatcher<Q> requestMatcher;

    @Getter
    @Setter
    @NonNull
    private RequestHandler<Q, S> requestHandler;

    @Getter
    @NonNull
    private Pipeline<Q, S> pipeline;

    protected ThreadPoolExecutor threadPool;

    protected Route(RouteBuilder<Q, S, ?, ?> b) {

        this.requestMatcher = b.requestMatcher;
        this.requestHandler = b.requestHandler;

        setPipeline(b.pipeline);

    }

    public static <Q extends Request, S extends Response> RouteBuilder<Q, S, ?, ?> builder() {
        return new RouteBuilderImpl<Q, S>();
    }

    public void setThreadPool(ThreadPoolExecutor threadPool) {

        this.threadPool = threadPool;

        this.pipeline.setThreadPool(threadPool);

    }

    public void setPipeline(Pipeline<Q, S> pipeline) {

        this.pipeline = pipeline;

        pipeline.setThreadPool(this.threadPool);

    }

    /**
     * Checks whether the route should handle the specified {@link Request}.
     *
     * @param request request to handle
     * @return true iff the route should handle the specified {@link Request}
     */
    public boolean shouldHandle(@NonNull Q request) {

        return requestMatcher.matches(request);

    }

    /**
     * Handles the specified {@link Request} via passing it through the route's {@link Pipeline}.
     *
     * @param request request to handle
     * @return the generated {@link Response}
     */
    public S handle(@NonNull Q request) {

        return pipeline.handle(request, requestHandler);

    }

    @SuppressWarnings({"checkstyle:ModifierOrder", "checkstyle:MissingJavadocType", "checkstyle:MissingJavadocMethod"})
    public static abstract class RouteBuilder<
        Q extends Request,
        S extends Response,
        C extends Route<Q, S>,
        B extends RouteBuilder<Q, S, C, B>
        > {

        @NonNull
        private RequestMatcher<Q> requestMatcher = CommonRequestMatchers.neverMatch();

        @NonNull
        private RequestHandler<Q, S> requestHandler;

        @NonNull
        private Pipeline<Q, S> pipeline = Pipeline.<Q, S>builder().build();

        public B requestMatcher(@NonNull RequestMatcher<Q> requestMatcher) {

            this.requestMatcher = requestMatcher;
            return self();

        }

        public B alwaysMatch() {

            return requestMatcher(CommonRequestMatchers.alwaysMatch());

        }

        public B requestHandler(@NonNull RequestHandler<Q, S> requestHandler) {

            this.requestHandler = requestHandler;
            return self();

        }

        public B handler(@NonNull Function<Q, S> handlerFunction) {

            this.requestHandler = request -> RequestHandlerPayload.ok(handlerFunction.apply(request));

            return self();

        }

        public B pipeline(@NonNull Pipeline<Q, S> pipeline) {

            this.pipeline = pipeline;
            return self();

        }

        protected abstract B self();

        public abstract C build();

    }

    private static final class RouteBuilderImpl<
        Q extends Request,
        S extends Response
        > extends RouteBuilder<
        Q,
        S,
        Route<Q, S>,
        RouteBuilderImpl<Q, S>
        > {

        private RouteBuilderImpl() {
        }

        protected Route.RouteBuilderImpl<Q, S> self() {

            return this;

        }

        public Route<Q, S> build() {

            return new Route<Q, S>(this);

        }

    }

}
