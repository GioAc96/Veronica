package rocks.gioac96.veronica.samples;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import rocks.gioac96.veronica.core.HttpMethod;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.core.Response;

public class HelloWorldTest extends E2ETest {

    @Override
    protected Map<Request.RequestBuilder, Consumer<Response>> getTestCases() {

        return new HashMap<>() {{

            put(
                mockRequest()
                    .path("/")
                    .httpMethod(HttpMethod.GET),
                response -> assertEquals(HelloWorld.getMessage(), response.getBody())
            );

            put(
                mockRequest()
                    .path("/some/path")
                    .httpMethod(HttpMethod.POST),
                response -> assertEquals(HelloWorld.getMessage(), response.getBody())
            );

            put(
                mockRequest()
                    .path("/some/other/path?query=myquery")
                    .httpMethod(HttpMethod.PUT),
                response -> assertEquals(HelloWorld.getMessage(), response.getBody())
            );

        }};

    }

    @Override
    protected RequestHandler getRequestHandler() {

        return new HelloWorld().getRequestHandler();

    }

}
