package rocks.gioac96.veronica.tutorials.multiple_servers;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.BasicApplicationFactory;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Router;

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

        Application<Request, Response> app = new MyApplication().build();

        app.start();

    }

}
