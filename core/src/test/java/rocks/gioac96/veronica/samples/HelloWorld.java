package rocks.gioac96.veronica.samples;

import lombok.Getter;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.RequestHandler;
import rocks.gioac96.veronica.Response;

public class HelloWorld {

    @Getter
    private static final String message = "Hello, world!";

    @Getter
    private final RequestHandler requestHandler;

    public HelloWorld() {

        this.requestHandler = request -> Response.builder()
            .body("Hello, world!")
            .provide();

    }

    public static void main(String[] args) {

        Application.builder()
            .port(80)
            .requestHandler(request -> Response.builder().body("Hello, world!").provide())
            .provide()
            .start();

    }

}
