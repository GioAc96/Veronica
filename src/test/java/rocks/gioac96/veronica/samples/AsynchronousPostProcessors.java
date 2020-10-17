package rocks.gioac96.veronica.samples;

import static rocks.gioac96.veronica.common.CommonRequestMatchers.get;

import lombok.Getter;
import rocks.gioac96.veronica.common.CommonRequestHandlers;
import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.Pipeline;
import rocks.gioac96.veronica.core.PostProcessor;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.core.Router;

public class AsynchronousPostProcessors {

    @Getter
    private final int sleepTime;

    @Getter
    private final Router router = Router.builder()
        .route(Route.builder()
            .requestMatcher(get("/async"))
            .requestHandler(Pipeline.builder()
                .requestHandler(request -> CommonResponses.ok())
                .postProcessor((PostProcessor.Asynchronous) (request, response) -> sleep())
                .provide())
            .provide())
        .route(Route.builder()
            .requestMatcher(get("/sync"))
            .requestHandler(Pipeline.builder()
                .requestHandler(CommonRequestHandlers.ok())
                .postProcessor((request, response) -> sleep())
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
