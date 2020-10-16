package rocks.gioac96.veronica.tutorials;

import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.ExceptionHandler;
import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.providers.CreationException;

public class ExceptionHandling {

    public static void main(String[] args) {

        Router router = Router.builder()
            .defaultRequestHandler(request -> {

                String numberQueryString = request.getQueryParam("number");

                int number = Integer.parseInt(numberQueryString);

                return Response.builder()
                    .body(String.valueOf(number * 2))
                    .provide();
            })
            .provide();

        ExceptionHandler exceptionHandler = new ExceptionHandler() {

            @Override
            public Response handle(Exception e) {

                return Response.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getClass().getName() + ": " + e.getMessage())
                    .provide();

            }

        };

        int port = 8000;

        try {

            Application app = Application.builder()
                .port(port)
                .router(router)
                .exceptionHandler(exceptionHandler)
                .provide();
            app.start();

        } catch (CreationException e) {

            System.out.println("Unable to start the application: " + e.getMessage());

        }

    }

}
