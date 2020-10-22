package rocks.gioac96.veronica.core.pipeline;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Response;

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

}