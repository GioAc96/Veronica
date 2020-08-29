package rocks.gioac96.veronica.samples;

import static rocks.gioac96.veronica.routing.matching.CommonRequestMatchers.favicon;
import static rocks.gioac96.veronica.routing.pipeline.stages.RequestHandlerPayload.ok;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.Server;
import rocks.gioac96.veronica.http.CommonResponses;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;
import rocks.gioac96.veronica.routing.pipeline.Pipeline;
import rocks.gioac96.veronica.routing.pipeline.stages.AsynchronousPostProcessor;

public class AsynchronousPostProcessors {

    public static void main(String[] args) {

        Application.basic()
            .server(Server.builder().port(80).build())
            .threads(1)
            .router(Router.builder()
                .route(
                    Route.builder()
                        .requestMatcher(favicon())
                        .requestHandler(request -> ok(CommonResponses.notFound()))
                        .build()
                )
                .fallbackRoute(Route.builder()
                    .requestHandler(request -> ok(CommonResponses.ok()))
                    .pipeline(
                        Pipeline.builder()
                            .postProcessor((AsynchronousPostProcessor<Request, Response>)(request, response) -> {
                                try {
                                    Thread.sleep(2000);
                                    System.out.println("Finished post processor");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            })
                            .build())
                    .build())
                .build())
            .build()
            .start();

    }

}
