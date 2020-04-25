package rocks.gioac96.veronica.samples.factories;

import rocks.gioac96.veronica.BasicApplicationFactory;
import rocks.gioac96.veronica.factories.CreationException;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;

public class MyApplication extends BasicApplicationFactory {

    public static void main(String[] args) throws CreationException {

        new MyApplication().build().start();

    }

    @Override
    public void configure() {

        port(8000);

        router(Router.builder()
            .fallbackRoute(Route.builder()
                .requestHandler(request -> Response.builder()
                    .body("test")
                    .build()
                )
                .build()
            )
            .build()
        );

    }

}
