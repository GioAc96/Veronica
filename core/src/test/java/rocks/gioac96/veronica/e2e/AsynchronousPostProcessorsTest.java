package rocks.gioac96.veronica.e2e;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static rocks.gioac96.veronica.e2e.E2ETest.measureTime;

import org.junit.jupiter.api.Test;
import rocks.gioac96.veronica.HttpMethod;
import rocks.gioac96.veronica.Request;
import rocks.gioac96.veronica.Router;
import rocks.gioac96.veronica.samples.AsynchronousPostProcessors;

public class AsynchronousPostProcessorsTest {

    private final int sleepTime = 50;
    private final Router router = new AsynchronousPostProcessors(sleepTime).getRouter();

    @Test
    void testDurationDifference() {

        Request syncRequest = E2ETest.mockRequest()
            .path("/sync")
            .httpMethod(HttpMethod.GET)
            .provide();
        Request asyncRequest = E2ETest.mockRequest()
            .path("/async")
            .httpMethod(HttpMethod.GET)
            .provide();

        long syncDuration = measureTime(() -> router.handle(syncRequest));
        long asyncDuration = measureTime(() -> router.handle(asyncRequest));

        assertTrue(syncDuration - asyncDuration > sleepTime * 0.9);

    }

}