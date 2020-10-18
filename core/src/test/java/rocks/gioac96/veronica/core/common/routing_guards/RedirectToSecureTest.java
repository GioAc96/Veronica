package rocks.gioac96.veronica.core.common.routing_guards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.RoutingGuard;

class RedirectToSecureTest {

    private static final RoutingGuard permanentRedirectToSecure = new RedirectToSecure()
        .permanent()
        .provide();

    private static final RoutingGuard temporaryRedirectToSecure = new RedirectToSecure()
        .temporary()
        .provide();
    private static final String secureUri = "https://test.com/";
    private static final String nonSecureUri = "http://test.com/";
    private static final Request secureRequest = mockRequest(true);
    private static final Request nonSecureRequest = mockRequest(false);

    private static Request mockRequest(boolean isSecure) {

        Request requestMock = mock(Request.class);

        when(requestMock.isSecure()).thenReturn(isSecure);

        URI uriMock = mock(URI.class);

        when(requestMock.getUri()).thenReturn(uriMock);

        if (isSecure) {

            when(uriMock.toString()).thenReturn(secureUri);

        } else {

            when(uriMock.toString()).thenReturn(nonSecureUri);

        }

        return requestMock;

    }

    @Test
    void testPermanentRedirectSecure() {

        assertNull(
            permanentRedirectToSecure.handle(secureRequest)
        );

    }

    @Test
    void testTemporaryRedirectSecure() {

        assertNull(
            temporaryRedirectToSecure.handle(secureRequest)
        );

    }

    @Test
    void testPermanentRedirectNonSecure() {

        Response response = permanentRedirectToSecure.handle(nonSecureRequest);

        String locationHeader = response.getHeaders().getFirst("Location");

        assertEquals(secureUri, locationHeader);
        Assertions.assertEquals(HttpStatus.MOVED_PERMANENTLY, response.getHttpStatus());

    }

    @Test
    void testTemporaryRedirectNonSecure() {

        Response response = temporaryRedirectToSecure.handle(nonSecureRequest);

        String locationHeader = response.getHeaders().getFirst("Location");

        assertEquals(secureUri, locationHeader);
        assertEquals(HttpStatus.TEMPORARY_REDIRECT, response.getHttpStatus());

    }

}