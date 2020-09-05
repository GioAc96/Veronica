package rocks.gioac96.veronica.samples;

import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.core.Router;

public class Benchmark {

    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        Application.builder()
            .port(80)
            .router(Router.builder()
                .defaultRoute(Route.builder().requestHandler(request -> CommonResponses.ok()).build())
                .build())
            .build()
            .start();

    }

}
