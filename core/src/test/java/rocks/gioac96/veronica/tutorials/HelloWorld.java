package rocks.gioac96.veronica.tutorials;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.RequestHandler;
import rocks.gioac96.veronica.Response;

public class HelloWorld {

    public static void main(String[] args) {

        RequestHandler helloWorldHandler = request -> Response.builder()
            .body("Hello World")
            .provide();

        int port = 8000;

        Application app = Application.builder()
            .port(port)
            .requestHandler(helloWorldHandler)
            .provide();

        app.start();


    }

}
