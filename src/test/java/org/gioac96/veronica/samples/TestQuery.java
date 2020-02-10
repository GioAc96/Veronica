package org.gioac96.veronica.samples;

import java.io.IOException;
import java.util.Map;
import org.gioac96.veronica.Application;
import org.gioac96.veronica.http.HttpStatus;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.routing.Route;
import org.gioac96.veronica.routing.Router;

public class TestQuery {

    public static void main(String[] args) throws IOException {

        Application application = new Application(80);

        Route fallback =  Route.builder()
            .requestHandler(request -> Response.builder()
                .httpStatus(HttpStatus.OK)
                .body("Try to set some query params!")
                .build()
            )
            .build();

        Router router = Router.builder()
            .fallbackRoute(fallback)
            .build();

        router.getRoutes().add(Route.builder()
            .requestMatcher(request -> request.getUri().getQuery() != null)
            .requestHandler(
                request -> {

                    StringBuilder bodyBuilder = new StringBuilder();

                    for (Map.Entry<String, String> queryParam : request.getQueryMap().entrySet()) {

                        if (queryParam.getValue().length() < 1) {

                            bodyBuilder
                                .append(queryParam.getKey())
                                .append(" -> true");

                        } else {

                            bodyBuilder
                                .append(queryParam.getKey())
                                .append(" -> ")
                                .append(queryParam.getValue());

                        }

                        bodyBuilder.append(System.getProperty("line.separator"));

                    }

                    return Response.builder()
                        .httpStatus(HttpStatus.OK)
                        .body(bodyBuilder.toString())
                        .build();

                }
            )
            .build()
        );

        application.setRouter(router);
        application.start();

    }

}
