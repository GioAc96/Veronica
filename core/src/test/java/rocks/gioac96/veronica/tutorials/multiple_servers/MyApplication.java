package rocks.gioac96.veronica.tutorials.multiple_servers;

import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.tutorials.https.handlers.MyHandler;

public class MyApplication extends Application.ApplicationBuilder  {

    public static void main(String[] args) {

        Application app = new MyApplication().provide();

        app.start();

    }

    @Override
    public void configure() {

        router(Router.builder()
            .defaultRequestHandler(new MyHandler())
            .provide());

        server(new MyServer());
        server(new MySecureServer());

    }

}
