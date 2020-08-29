package rocks.gioac96.veronica.samples;

import static rocks.gioac96.veronica.routing.matching.CommonRequestMatchers.get;
import static rocks.gioac96.veronica.routing.pipeline.stages.RequestHandlerPayload.ok;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.http.CommonResponses;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.CommonRoutes;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;
import rocks.gioac96.veronica.routing.pipeline.Pipeline;
import rocks.gioac96.veronica.routing.pipeline.stages.PostProcessor;

public class AsynchronousPostProcessors {

    private static void sleep() {

        try {

            Thread.sleep(2000);

            System.out.println("Finished");

        } catch (InterruptedException ignored) {}

    }

    public static void main(String[] args) {

        Application.basic()
            .port(80)
            .router(Router.builder()
                .route(Route.builder()
                    .requestMatcher(get("/async"))
                    .handler(request -> CommonResponses.ok())
                    .pipeline(Pipeline.builder()
                        .postProcessor((PostProcessor.Asynchronous<Request, Response>)((request, response) -> sleep()))
                        .build())
                    .build())
                .route(Route.builder()
                    .requestMatcher(get("/sync"))
                    .handler(request -> CommonResponses.ok())
                    .pipeline(Pipeline.builder()
                        .postProcessor((request, response) -> sleep())
                        .build())
                    .build())
                .fallbackRoute(CommonRoutes.notFound())
                .build())
            .build()
            .start();

    }

}
