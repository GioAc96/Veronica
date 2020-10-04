package rocks.gioac96.veronica.e2e;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import rocks.gioac96.veronica.core.HttpMethod;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.samples.Path;

public class PathTest extends E2ETest {

    @Override
    protected Map<Request.RequestBuilder, Consumer<Response>> getTestCases() {

        String[] paths = new String[] {

            "/path",
            "/other/path",
            "/this/is/the/path",
            "/"

        };

        return new HashMap<>() {{

            Arrays.stream(paths).forEach(path -> {

                put(
                    mockRequest()
                        .path(path)
                        .httpMethod(HttpMethod.GET),
                    response -> assertResponseBodyEquals(Path.message + path, response)
                );

            });

        }};

    }

    @Override
    protected Router getRouter() {

        return new Path().getRouter();

    }

}
