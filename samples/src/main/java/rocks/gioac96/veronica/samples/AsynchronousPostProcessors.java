package rocks.gioac96.veronica.samples;

import lombok.Getter;
import rocks.gioac96.veronica.core.common.CommonRequestHandlers;
import rocks.gioac96.veronica.core.common.CommonResponses;
import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;
import rocks.gioac96.veronica.routing.common.CommonRequestMatchers;
import rocks.gioac96.veronica.core.pipeline.Pipeline;

public class AsynchronousPostProcessors {

    @Getter
    private final int sleepTime;

    @Getter
    private final Router router = Router.builder()
        .route(Route.builder()
            .requestMatcher(CommonRequestMatchers.get("/async"))
            .requestHandler(Pipeline.builder()
                .postProcessor((request, response, pipelineData) -> {
                    sleep();
                })
                .stage((request, responseBuilder, data) -> CommonResponses.ok())
                .provide())
            .provide())
        .route(Route.builder()
            .requestMatcher(CommonRequestMatchers.get("/sync"))
            .requestHandler(Pipeline.builder()
                .stage((request, responseBuilder, data) -> {
                    sleep();
                    return null;
                })
                .stage((request, responseBuilder, data) -> CommonResponses.ok())
                .provide())
            .provide())
        .defaultRequestHandler(CommonRequestHandlers.notFound())
        .provide();

    public AsynchronousPostProcessors(
        int sleepTime
    ) {

        this.sleepTime = sleepTime;

    }

    public static void main(String[] args) {

        Application.builder()
            .port(80)
            .requestHandler(new AsynchronousPostProcessors(2000).router)
            .provide()
            .start();

    }

    private void sleep() {

        try {

            Thread.sleep(sleepTime);

        } catch (InterruptedException ignored) {
        }

    }

}
