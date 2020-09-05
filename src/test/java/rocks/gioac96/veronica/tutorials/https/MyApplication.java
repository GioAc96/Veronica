package rocks.gioac96.veronica.tutorials.https;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.tutorials.https.routes.RouteA;

public class MyApplication extends Application.ApplicationBuilder {

    @Override
    public void configure() {

        server(new MyServer());

        router(Router.builder()
            .defaultRoute(new RouteA())
            .build()
        );

    }

    public static void main(String[] args) {

        Application app = new MyApplication().build();

        app.start();

    }


}
