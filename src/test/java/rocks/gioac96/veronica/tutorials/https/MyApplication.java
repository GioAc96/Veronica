package rocks.gioac96.veronica.tutorials.https;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.BasicApplicationFactory;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Router;
import rocks.gioac96.veronica.tutorials.https.routes.RouteA;
import rocks.gioac96.veronica.tutorials.https.ssl.MyContext;

public class MyApplication extends BasicApplicationFactory {

    @Override
    public void configure() {

        port(443);

        router(Router.builder()
            .fallbackRoute(new RouteA().build())
            .build()
        );

        sslContext(new MyContext());

    }

    public static void main(String[] args) {

        Application<Request, Response> app = new MyApplication().build();

        app.start();

    }


}
