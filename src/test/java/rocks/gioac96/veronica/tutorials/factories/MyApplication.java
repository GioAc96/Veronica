package rocks.gioac96.veronica.tutorials.factories;


import rocks.gioac96.veronica.BasicApplicationFactory;
import rocks.gioac96.veronica.Server;

public class MyApplication extends BasicApplicationFactory {

    @Override
    public void configure() {

        router(new MyRouter());
        port(80);

    }

}
