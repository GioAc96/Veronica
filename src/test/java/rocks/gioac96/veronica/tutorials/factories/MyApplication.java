package rocks.gioac96.veronica.tutorials.factories;


public class MyApplication extends BasicApplicationFactory {

    @Override
    public void configure() {

        router(new MyRouter());
        port(80);

    }

}
