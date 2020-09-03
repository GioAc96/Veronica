package rocks.gioac96.veronica.tutorials.multiple_servers;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.core.Router;

public class MyApplication extends BasicApplicationFactory {

    @Override
    public void configure() {

        router(Router.builder()
            .fallbackRoute(new RouteA().build())
            .build());

        server(new MyServer());
        server(new MySecureServer());

    }

    public static void main(String[] args) {

        Application app = new MyApplication().build();

        app.start();

    }

}
