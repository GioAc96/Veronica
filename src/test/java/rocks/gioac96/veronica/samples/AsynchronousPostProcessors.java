package rocks.gioac96.veronica.samples;

import static rocks.gioac96.veronica.common.CommonRequestMatchers.get;

import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.common.CommonRoutes;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.core.Pipeline;
import rocks.gioac96.veronica.core.PostProcessor;

public class AsynchronousPostProcessors {

    private static void sleep() {

        try {

            Thread.sleep(2000);

            System.out.println("Finished");

        } catch (InterruptedException ignored) {}

    }

    public static void main(String[] args) {

        Application.builder()
            .port(80)
            .router(Router.builder()
                .route(Route.builder()
                    .requestMatcher(get("/async"))
                    .requestHandler(request -> CommonResponses.ok())
                    .pipeline(Pipeline.builder()
                        .postProcessor((PostProcessor.Asynchronous)((request, response) -> sleep()))
                        .build())
                    .build())
                .route(Route.builder()
                    .requestMatcher(get("/sync"))
                    .requestHandler(request -> CommonResponses.ok())
                    .pipeline(Pipeline.builder()
                        .postProcessor((request, response) -> sleep())
                        .build())
                    .build())
                .defaultRoute(CommonRoutes.notFound())
                .build())
            .build()
            .start();

    }

}
