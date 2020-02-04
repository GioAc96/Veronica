package org.gioac96.veronica.routing;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.routing.pipeline.Pipeline;
import org.gioac96.veronica.routing.pipeline.RequestHandler;
import org.gioac96.veronica.routing.pipeline.ResponseRenderer;

/**
 * Application route.
 */
@Builder
public class Route {

    @NonNull
    @Getter
    @Setter
    private RequestMatcher requestMatcher;

    @Getter
    @Setter
    @NonNull
    private RequestHandler requestHandler;

    @Getter
    @Setter
    @NonNull
    private Pipeline pipeline;

    public Route(
        @NonNull RequestHandler requestHandler,
        @NonNull ResponseRenderer responseRenderer
    ) {

        this.requestMatcher = new RequestMatcher(request -> true);
        this.requestHandler = requestHandler;
        this.pipeline = new Pipeline(responseRenderer);

    }

    public Route(
        @NonNull RequestHandler requestHandler,
        @NonNull Pipeline pipeline
    ) {

        this.requestMatcher = new RequestMatcher(request -> true);
        this.requestHandler = requestHandler;
        this.pipeline = pipeline;

    }

    public Route(
        @NonNull RequestHandler requestHandler,
        @NonNull RequestMatcher requestMatcher,
        @NonNull ResponseRenderer responseRenderer
    ) {

        this.requestMatcher = requestMatcher;
        this.requestHandler = requestHandler;
        this.pipeline = new Pipeline(responseRenderer);

    }

    public Route(
        @NonNull RequestHandler requestHandler,
        @NonNull RequestMatcher requestMatcher,
        @NonNull Pipeline pipeline
    ) {

        this.requestMatcher = requestMatcher;
        this.requestHandler = requestHandler;
        this.pipeline = pipeline;

    }

    /**
     * Checks wether the route should handle the specified {@link Request}.
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

}
