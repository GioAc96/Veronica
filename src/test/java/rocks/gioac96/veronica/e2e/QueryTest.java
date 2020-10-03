package rocks.gioac96.veronica.e2e;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import rocks.gioac96.veronica.core.HttpMethod;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.samples.Query;

public class QueryTest extends E2ETest {

    @Override
    protected Map<Request.RequestBuilder, Consumer<Response>> getTestCases() {

        return new HashMap<>(){{

            put(

                mockRequest()
                    .path("/")
                    .query("")
                    .httpMethod(HttpMethod.GET),

                response -> assertArrayEquals(new HashMap<>().toString().getBytes(), response.getBody())
            );

            put(
                mockRequest()
                    .path("/")
                    .query("var1=val1")
                    .httpMethod(HttpMethod.GET),

                response -> assertArrayEquals(new HashMap<>(){{

                    put("var1", "val1");

                }}.toString().getBytes(), response.getBody())
            );

            put(
                mockRequest()
                    .path("/")
                    .query("var1=val1&var2=second_value")
                    .httpMethod(HttpMethod.GET),

                response -> assertArrayEquals(new HashMap<>(){{

                    put("var1", "val1");
                    put("var2", "second_value");

                }}.toString().getBytes(), response.getBody())
            );

            put(
                mockRequest()
                    .path("/")
                    .query("var1=val1&var2=second_value&bool")
                    .httpMethod(HttpMethod.GET),

                response -> assertArrayEquals(new HashMap<>(){{

                    put("var1", "val1");
                    put("var2", "second_value");
                    put("bool", "");

                }}.toString().getBytes(), response.getBody())
            );


        }};

    }

    @Override
    protected Router getRouter() {

        return new Query().getRouter();

    }

}
