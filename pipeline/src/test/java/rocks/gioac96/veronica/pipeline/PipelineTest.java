package rocks.gioac96.veronica.pipeline;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.concurrency.PriorityFixedThreadPoolExecutor;

class PipelineTest {

    private final static Random random = new Random();

    @ParameterizedTest
    @ValueSource(ints = 100)
    void testStageExecutedRightOrder(int testSize) {

        Pipeline.PipelineBuilder<Object> pipelineBuilder = Pipeline.builder();
        List<Integer> executionOrder = new ArrayList<>(testSize + 1);

        int priorityBound = testSize / 5;

        for (int i = 0; i < testSize; i++) {

            int priority = random.nextInt(priorityBound);

            pipelineBuilder.stage((request, responseBuilder, data) -> {

                executionOrder.add(priority);
                return null;

            }, priority);

        }

        Response response = Response.builder().provide();

        pipelineBuilder.stage(((request, responseBuilder, data) -> {

            executionOrder.add(priorityBound);
            return response;

        }), priorityBound);

        Response actualResponse = pipelineBuilder.provide().handle(null);

        int lastP = -1;

        for (Integer executed : executionOrder) {

            assertTrue(lastP <= executed, "Wrong execution order");
            lastP = executed;

        }

        assertSame(response, actualResponse);

    }

    @Test
    void testStageInterruptsPipelineNoPriority() {

        Response r2 = Response.builder().provide();
        Response r3 = Response.builder().provide();

        Pipeline<Object> pipeline = Pipeline.builder()
            .stage((request, responseBuilder, data) -> null)
            .stage((request, responseBuilder, data) -> r2)
            .stage((request, responseBuilder, data) -> r3)
            .provide();

        assertSame(r2, pipeline.handle(null));
        assertNotSame(r3, pipeline.handle(null));

    }

    @Test
    void testStageInterruptsPipeline() {

        Response r2 = Response.builder().provide();
        Response r3 = Response.builder().provide();

        Pipeline<Object> pipeline = Pipeline.builder()
            .stage((request, responseBuilder, data) -> null, 1)
            .stage((request, responseBuilder, data) -> r3, 1000)
            .stage((request, responseBuilder, data) -> r2, 5)
            .provide();

        assertSame(r2, pipeline.handle(null));
        assertNotSame(r3, pipeline.handle(null));

    }

    @Test
    void testBuilderCalls() {

        Pipeline<Object> pipeline = Pipeline.builder()
            .stage((request, responseBuilder, data) -> {

                responseBuilder.httpStatus(HttpStatus.NOT_FOUND);
                return null;

            })
            .stage((request, responseBuilder, data) -> {

                responseBuilder.body("Hola");
                return null;

            })
            .provide();

        Response expected = Response.builder()
            .httpStatus(HttpStatus.NOT_FOUND)
            .body("Hola")
            .provide();

        assertEquals(expected, pipeline.handle(null));
        assertNotSame(expected, pipeline.handle(null));

    }

    @Test
    void testNoPostPipelineStages() {

        assertEquals(
            Response.builder().provide(),
            Pipeline.builder().provide().handle(null)
        );

    }

    @Test
    void testResponseBuilderFactory() {

        PipelineResponseBuilderFactory factory = () -> Response.builder()
            .httpStatus(HttpStatus.CONFLICT);

        Response expected = Response.builder()
            .httpStatus(HttpStatus.CONFLICT)
            .body("veronica<3")
            .provide();

        assertEquals(
            expected,
            Pipeline.builder()
                .responseBuilderFactory(factory)
                .stage((request, responseBuilder, data) -> {

                    responseBuilder.body("veronica<3");

                    return null;

                })
                .provide().handle(null)
        );

    }

    @ParameterizedTest
    @ValueSource(ints = 100)
    void testPostProcessorsSchedulingOrder(int testSize) throws InterruptedException {

        @AllArgsConstructor
        class PostProcessorWithSchedulingOrder implements PostProcessor<Object> {

            final int schedulingOrder;

            @Override
            public void process(Request request, Response response, Object pipelineData) {

            }
        }


        int priorityBound = testSize / 5;

        Pipeline.PipelineBuilder<Object> pipelineBuilder = Pipeline.builder();

        for (int i = 0; i < testSize; i++) {

            int priority = random.nextInt(priorityBound);

            pipelineBuilder.postProcessor(
                new PostProcessorWithSchedulingOrder(priority),
                priority
            );

        }

        PriorityFixedThreadPoolExecutor executor = mock(PriorityFixedThreadPoolExecutor.class);
        List<PostProcessor<Object>> sortedPostProcessors = pipelineBuilder.generatePostProcessorsList();

        int lastP = -1;

        for (PostProcessor<Object> sortedPostProcessor : sortedPostProcessors) {

            assertTrue(lastP <= ((PostProcessorWithSchedulingOrder) sortedPostProcessor).schedulingOrder, "Wrong scheduling order");
            lastP = ((PostProcessorWithSchedulingOrder) sortedPostProcessor).schedulingOrder;

        }

    }
    
}