package rocks.gioac96.veronica.tutorials.factories;


import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;

public class MyApplication extends Application.ApplicationBuilder implements BuildsSingleInstance {

    @Override
    public void configure() {

        router(new MyRouter());
        port(80);

    }

}
