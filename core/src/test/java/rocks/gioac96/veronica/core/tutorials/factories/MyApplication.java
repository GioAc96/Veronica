package rocks.gioac96.veronica.core.tutorials.factories;


import rocks.gioac96.veronica.core.Application;

public class MyApplication extends Application.ApplicationBuilder {

    @Override
    public void configure() {

        router(new MyRouter());
        port(80);

    }

}
