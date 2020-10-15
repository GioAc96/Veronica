package rocks.gioac96.veronica.tutorials;

import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.core.Response;

public class HelloWorld {

    public static void main(String[] args) {

        RequestHandler helloWorldHandler = request -> Response.builder()
            .body("Hello World")
            .build();

        int port = 8000;

        Application app = Application.builder()
            .port(port)
            .requestHandler(helloWorldHandler)
            .build();

        app.start();


    }

}
