package rocks.gioac96.veronica.tutorials.https;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.tutorials.https.routes.RouteA;

public class MyApplication extends BasicApplicationFactory {

    @Override
    public void configure() {

        server(new MyServer());

        router(Router.builder()
            .fallbackRoute(new RouteA().build())
            .build()
        );

    }

    public static void main(String[] args) {

        Application app = new MyApplication().build();

        app.start();

    }


}
