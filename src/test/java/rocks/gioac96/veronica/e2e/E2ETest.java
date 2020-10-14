package rocks.gioac96.veronica.e2e;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.MockUtil.isMock;

import com.sun.net.httpserver.Headers;
import java.net.URI;
import java.util.Map;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;

public abstract class E2ETest {

    protected static class MockRequestBuilder extends Request.RequestBuilder implements BuildsMultipleInstances {

        private String path = null;
        private String query = null;

        public MockRequestBuilder path(String path) {

            this.path = path;

            URI uriMock = mock(URI.class);

            when(uriMock.getPath()).thenReturn(path);

            uri(uriMock);

            return this;

        }

        public MockRequestBuilder query(String query) {

            this.query = query;

            if (getUri() == null) {


                URI uriMock = mock(URI.class);

                when(uriMock.getQuery()).thenReturn(query);

                uri(uriMock);


            } else {

                when(getUri().getQuery()).thenReturn(query);

            }

            return this;

        }

        public MockRequestBuilder header(String name, String value) {

            if (! isMock(getHeaders())) {

                headers(mock(Headers.class));

            }

            when(getHeaders().getFirst(name)).thenReturn(value);

            return this;

        }

        @Override
        protected boolean isValid() {

            return true;

        }

        @Override
        public String toString() {

            StringBuilder sb = new StringBuilder("Request:");

            sb.append("\n\tmethod: ").append(getHttpMethod());
            sb.append("\n\tpath: ").append(path);
            sb.append("\n\tquery: ").append(query);
            sb.append("\n\tbody: ").append(getBody());
            sb.append("\n\tsecure: ").append(isSecure());

            return sb.toString();

        }

    }

    protected abstract Map<Request.RequestBuilder, Consumer<Response>> getTestCases();

    protected abstract Router getRouter();

    protected static long measureTime(Runnable action) {

        long start = System.currentTimeMillis();

        action.run();

        long end = System.currentTimeMillis();

        return end - start;

    }

    protected static MockRequestBuilder mockRequest() {

        return new MockRequestBuilder();

    }

    protected static void assertResponseBodyEquals(String expected, Response response) {

        assertEquals(expected, response.getBody());

    }

    @Test
    void runTest() {

        for (Map.Entry<Request.RequestBuilder, Consumer<Response>> testCase : getTestCases().entrySet()) {

            Request request = testCase.getKey().build();
            Consumer<Response> assertions = testCase.getValue();

            try {

                assertions.accept(getRouter().handle(request));

            } catch(AssertionError assertionError) {

                System.out.println("Failed on request: " + testCase.getKey().toString());

                throw assertionError;

            }

        }

    }

}
