package rocks.gioac96.veronica.common.request_matchers;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rocks.gioac96.veronica.core.HttpMethod;
import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Router;

public class FaviconTest {

    private Router router;

    private static final Response faviconResponse =
        Response.builder().httpStatus(HttpStatus.OK).build();

    private static final Response defaultResponse =
        Response.builder().httpStatus(HttpStatus.NOT_FOUND).build();

    private static final RequestHandler faviconRequestHandler =
        request -> faviconResponse;

    private static final RequestHandler defaultRequestHandler =
        request -> defaultResponse;

    @BeforeEach
    void setUp() {

        router = Router.builder()
            .route(new Favicon().build(), faviconRequestHandler)
            .defaultRequestHandler(defaultRequestHandler)
            .build();

    }

    @Test
    void testMatch() {

        Request request = Mockito.mock(Request.class);

        when(request.getHttpMethod()).thenReturn(HttpMethod.GET);
        when(request.getPath()).thenReturn("/favicon.ico");

        assertSame(faviconResponse, router.handle(request));

    }

    @Test
    void testWrongMethod() {

        Arrays.stream(HttpMethod.values()).filter(httpMethod -> httpMethod != HttpMethod.GET).forEach(
            httpMethod -> {
                Request request = Mockito.mock(Request.class);

                when(request.getHttpMethod()).thenReturn(httpMethod);
                when(request.getPath()).thenReturn("/favicon.ico");

                assertSame(
                    defaultResponse,
                    router.handle(request),
                    "Should not match method " + httpMethod
                );

            }
        );

    }

    @Test
    void testWrongPath() {

        List.of(
            "/",
            "/test",
            "/favicon",
            "/favicon.ic",
            "/someweirdpath",
            "some even weirder path ?",
            "/test?favicon.ico",
            "/#/favicon.ico",
            "afavicon.ico"
        ).forEach(
            path -> {
                Request request = Mockito.mock(Request.class);

                when(request.getHttpMethod()).thenReturn(HttpMethod.GET);
                when(request.getPath()).thenReturn(path);

                assertSame(
                    defaultResponse,
                    router.handle(request),
                    "Should not match path " + path
                );

            }
        );

    }

}