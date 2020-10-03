package rocks.gioac96.veronica.samples;

import lombok.Getter;
import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.core.Response;

public class HelloWorld {

    @Getter
    private Router router;

    @Getter
    private static final String message = "Hello, world!";

    public HelloWorld() {

        this.router = Router.builder()
            .defaultRequestHandler(request -> Response.builder()
                .body(message)
                .build()
            ).build();

    }

    public static void main(String[] args) {

        Application application = Application.builder()
            .port(80)
            .router(new HelloWorld().router)
            .build();

        application.start();

    }

}
