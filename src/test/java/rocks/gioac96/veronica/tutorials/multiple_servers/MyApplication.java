package rocks.gioac96.veronica.tutorials.multiple_servers;

import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.Router;

public class MyApplication extends Application.ApplicationBuilder {

    @Override
    public void configure() {

        router(Router.builder()
            .defaultRoute(new RouteA())
            .build());

        server(new MyServer());
        server(new MySecureServer());

    }

    public static void main(String[] args) {

        Application app = new MyApplication().build();

        app.start();

    }

}
