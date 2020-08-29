package rocks.gioac96.veronica.samples;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.http.CommonResponses;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;

public class Benchmark {

    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        Application.basic()
            .port(80)
            .router(Router.builder()
                .fallbackRoute(Route.builder().handler(request -> CommonResponses.ok()).build())
                .build())
            .build()
            .start();


    }

}
