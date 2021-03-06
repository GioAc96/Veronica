package rocks.gioac96.veronica.tutorials.https;

import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;
import rocks.gioac96.veronica.tutorials.https.handlers.MyHandler;

public class MyApplication extends Application.ApplicationBuilder implements BuildsSingleInstance {

    @Override
    public void configure() {

        server(new MyServer());

        router(Router.builder()
            .defaultRequestHandler(new MyHandler())
            .build()
        );

    }

    public static void main(String[] args) {

        Application app = new MyApplication().build();

        app.start();

    }

}
