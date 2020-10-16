package rocks.gioac96.veronica.tutorials.multiple_servers;

import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;
import rocks.gioac96.veronica.tutorials.https.handlers.MyHandler;

public class MyApplication extends Application.ApplicationBuilder implements BuildsSingleInstance {

    public static void main(String[] args) {

        Application app = new MyApplication().build();

        app.start();

    }

    @Override
    public void configure() {

        router(Router.builder()
            .defaultRequestHandler(new MyHandler())
            .build());

        server(new MyServer());
        server(new MySecureServer());

    }

}
