package rocks.gioac96.veronica.samples;

import static rocks.gioac96.veronica.common.CommonRequestMatchers.get;

import lombok.Getter;
import rocks.gioac96.veronica.common.CommonRequestHandlers;
import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.pipeline.Pipeline;
import rocks.gioac96.veronica.Route;
import rocks.gioac96.veronica.Router;

public class AsynchronousPostProcessors {

    @Getter
    private final int sleepTime;

    @Getter
    private final Router router = Router.builder()
        .route(Route.builder()
            .requestMatcher(get("/async"))
            .requestHandler(Pipeline.builder()
                .postProcessor((request, response, pipelineData) -> {
                    sleep();
                })
                .stage((request, responseBuilder, data) -> CommonResponses.ok())
                .provide())
            .provide())
        .route(Route.builder()
            .requestMatcher(get("/sync"))
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
            .router(new AsynchronousPostProcessors(2000).router)
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
