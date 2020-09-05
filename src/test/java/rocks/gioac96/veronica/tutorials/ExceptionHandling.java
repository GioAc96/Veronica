package rocks.gioac96.veronica.tutorials;

import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.core.ExceptionHandler;
import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.core.Router;

public class ExceptionHandling {

    public static void main(String[] args) {

        Route route = Route.builder()
            .requestHandler(request -> {

                String numberQueryString = request.getQueryParam("number");

                int number = Integer.parseInt(numberQueryString);

                return Response.builder()
                    .body(String.valueOf(number * 2))
                    .build();
            })
            .build();

        Router router = Router.builder()
            .defaultRoute(route)
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

            Application app = Application.builder()
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
