package rocks.gioac96.veronica.tutorials;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.Response;
import rocks.gioac96.veronica.Router;

public class Multithreading {

    public static void main(String[] args) {

        int port = 8000;

        Application app = Application.builder()
            .port(port)
            .router(Router.builder()
                .defaultRequestHandler(request -> {

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ignored) {
                    }

                    return Response.builder()
                        .body("Hello, there!")
                        .provide();

                })
                .provide()
            ).provide();

        app.start();

    }

}