package rocks.gioac96.veronica.tutorials.https;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.Router;
import rocks.gioac96.veronica.tutorials.https.handlers.MyHandler;

public class MyApplication extends Application.ApplicationBuilder  {

    public static void main(String[] args) {

        Application app = new MyApplication().provide();

        app.start();

    }

    @Override
    public void configure() {

        server(new MyServer());

        router(Router.builder()
            .defaultRequestHandler(new MyHandler())
            .provide()
        );

    }

}
