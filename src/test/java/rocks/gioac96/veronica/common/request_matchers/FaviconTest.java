package rocks.gioac96.veronica.common.request_matchers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rocks.gioac96.veronica.core.HttpMethod;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.RequestMatcher;
import rocks.gioac96.veronica.core.Route;

public class FaviconTest {

    private RequestMatcher faviconRequestMatcher;

    public FaviconTest() {

        faviconRequestMatcher = new Favicon().build();

    }

    public FaviconTest(Route route) {

        faviconRequestMatcher = route.getRequestMatcher();

    }

    @Test
    void testMatch() {

        Request request = Mockito.mock(Request.class);

        when(request.getHttpMethod()).thenReturn(HttpMethod.GET);
        when(request.getPath()).thenReturn("/favicon.ico");

        assertTrue(faviconRequestMatcher.matches(request));

    }

    @Test
    void testWrongMethod() {

        Arrays.stream(HttpMethod.values()).filter(httpMethod -> httpMethod != HttpMethod.GET).forEach(
            httpMethod -> {
                Request request = Mockito.mock(Request.class);

                when(request.getHttpMethod()).thenReturn(httpMethod);
                when(request.getPath()).thenReturn("/favicon.ico");

                assertFalse(faviconRequestMatcher.matches(request), "Should not match method " + httpMethod);

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

                assertFalse(faviconRequestMatcher.matches(request), "Should not match path " + path);

            }
        );

    }

}