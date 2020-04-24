package rocks.gioac96.veronica.tutorials;

import java.io.IOException;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.http.ExceptionHandler;
import rocks.gioac96.veronica.http.HttpStatus;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;

public class ExceptionHandling {

    public static void main(String[] args) {

        Route<Request, Response> route = Route.builder()
            .requestHandler(request -> {

                String numberQueryString = request.getQueryParam("number");

                if (numberQueryString == null) {

                    return Response.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .body("Try setting the query parameter for \"number\"")
                        .build();

                } else {

                    int number = Integer.parseInt(numberQueryString);

                    return Response.builder()
                        .body(String.valueOf(number * 2))
                        .build();

                }

            })
            .build();

        Router<Request, Response> router = Router.builder()
            .fallbackRoute(route)
            .build();

        ExceptionHandler exceptionHandler = new ExceptionHandler() {

            @Override
            public Response handle(Exception e) {

                return Response.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getClass().getName() + ": " + e.getMessage())
                    .build();

            }

        };

        int port = 8000;

        try {

            Application<Request, Response> app = Application.basic()
                .port(port)
                .router(router)
                .exceptionHandler(exceptionHandler)
                .build();
            app.start();

        } catch (IOException e) {

            System.out.println("Unable to start the application: " + e.getMessage());

        }

    }

}
