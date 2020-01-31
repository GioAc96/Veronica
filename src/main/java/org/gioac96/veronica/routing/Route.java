package org.gioac96.veronica.routing;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.routing.pipeline.RequestHandler;

public class Route {

    @NonNull
    @Getter
    @Setter
    private RequestMatcher requestMatcher;

    @Getter
    @Setter
    @NonNull
    private RequestHandler requestHandler;

    public Route(@NonNull RequestHandler requestHandler) {

        this.requestMatcher = request -> true;
        this.requestHandler = requestHandler;

    }

    public Route(
        @NonNull RequestMatcher requestMatcher,
        @NonNull RequestHandler requestHandler
    ) {

        this.requestMatcher = requestMatcher;
        this.requestHandler = requestHandler;

    }

    public boolean shouldHandle(Request request) {

        return requestMatcher.matches(request);

    }

    public Response handle(Request request) {

        return requestHandler.handle(request);

    }

}
