package rocks.gioac96.veronica.e2e;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import rocks.gioac96.veronica.core.HttpMethod;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.samples.Echo;

public class EchoTest extends E2ETest {

    @Override
    protected Map<Request.RequestBuilder, Consumer<Response>> getTestCases() {

        String[] bodyValues = new String[]{
          "body",
          "request body",
          "this is the request body"
        };

        HashMap<Request.RequestBuilder, Consumer<Response>> testCases = new HashMap<>() {{

            // No request body
            put(
                mockRequest()
                    .path("/")
                    .body("")
                    .httpMethod(HttpMethod.GET),
                response -> assertArrayEquals(
                    Echo.getErrorMessage().getBytes(),
                    response.getBody()
                )
            );


        }};

        for (String bodyValue : bodyValues) {

            testCases.put(
                mockRequest()
                    .path("/")
                    .body(bodyValue)
                    .httpMethod(HttpMethod.GET),
                response -> assertArrayEquals(
                    bodyValue.getBytes(),
                    response.getBody()
                )
            );

        }

        return testCases;

    }

    @Override
    protected Router getRouter() {

        return new Echo().getRouter();

    }

}
