package org.gioac96.veronica.routing;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.routing.pipeline.Pipeline;
import org.gioac96.veronica.routing.pipeline.RequestHandler;
import org.gioac96.veronica.routing.pipeline.ResponseRenderer;

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

        this.requestMatcher = request -> true;
        this.requestHandler = requestHandler;
        this.pipeline = new Pipeline(responseRenderer);

    }

    public Route(
        @NonNull RequestHandler requestHandler,
        @NonNull Pipeline pipeline
    ) {

        this.requestMatcher = request -> true;
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

    public boolean shouldHandle(@NonNull Request request) {

        return requestMatcher.matches(request);

    }

    public Response handle(@NonNull Request request) {

        return pipeline.handle(request, requestHandler);

    }

}
