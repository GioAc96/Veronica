package rocks.gioac96.veronica.tutorials;

import static rocks.gioac96.veronica.routing.pipeline.stages.RequestHandlerPayload.ok;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.Server;
import rocks.gioac96.veronica.factories.CreationException;
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

                int number = Integer.parseInt(numberQueryString);

                return ok(Response.builder()
                    .body(String.valueOf(number * 2))
                    .build());


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

        } catch (CreationException e) {

            System.out.println("Unable to start the application: " + e.getMessage());

        }

    }

}
