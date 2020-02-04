package org.gioac96.veronica.samples;

import java.io.IOException;
import lombok.Getter;
import lombok.NonNull;
import org.gioac96.veronica.Application;
import org.gioac96.veronica.http.HttpStatus;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.routing.RequestMatcher;
import org.gioac96.veronica.routing.Route;
import org.gioac96.veronica.routing.Router;
import org.gioac96.veronica.routing.pipeline.Pipeline;
import org.gioac96.veronica.routing.pipeline.ResponseRenderer;

public class Echo {

    public static void main(String[] args) throws IOException {

        Application application = new Application(80);

        ResponseRenderer responseRenderer = response -> ((TextResponse) response).getData();

        Route fallback = new Route(
            request -> new TextResponse(HttpStatus.OK, "Try to specify a request body"),
            responseRenderer
        );

        Route echo = Route.builder()
            .requestHandler(
                request -> Response.builder()
                    .httpStatus(HttpStatus.OK)
                    .body(request.getBody())
                    .build()
            )
            .requestMatcher(
                new RequestMatcher(request -> request.getBody().length() > 0)
            )
            .pipeline(
                new Pipeline(responseRenderer)
            )
            .build();

        Router router = new Router(fallback);

        router.getRoutes().add(echo, 0);

        application.setRouter(router);

        application.start();

    }

    private static class TextResponse extends Response {

        @Getter
        private String data;

        public TextResponse(@NonNull HttpStatus httpStatus, String data) {

            super(httpStatus);
            this.data = data;

        }

    }

}
