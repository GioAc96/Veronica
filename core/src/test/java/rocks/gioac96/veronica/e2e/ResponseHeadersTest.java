package rocks.gioac96.veronica.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import rocks.gioac96.veronica.HttpMethod;
import rocks.gioac96.veronica.Request;
import rocks.gioac96.veronica.Response;
import rocks.gioac96.veronica.Router;
import rocks.gioac96.veronica.samples.ResponseHeaders;

public class ResponseHeadersTest extends E2ETest {

    @Override
    protected Map<Request.RequestBuilder, Consumer<Response>> getTestCases() {

        return new HashMap<>() {{

            put(
                mockRequest()
                    .path("/")
                    .httpMethod(HttpMethod.GET),
                response -> assertEquals("application/json", response.getHeaders().get("content-type").stream().findFirst().get())
            );

        }};

    }

    @Override
    protected Router getRequestHandler() {

        return new ResponseHeaders().getRouter();

    }

}
