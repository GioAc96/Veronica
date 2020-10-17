package rocks.gioac96.veronica.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static rocks.gioac96.veronica.core.HttpStatus.OK;
import static rocks.gioac96.veronica.core.HttpStatus.UNAUTHORIZED;

import java.util.Base64;
import org.junit.jupiter.api.Test;
import rocks.gioac96.veronica.core.HttpMethod;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.samples.BasicAuth;

public class BasicAuthTest {

    String realm = "testRealm";

    Request unauthorizedRequest = E2ETest.mockRequest()
        .path("/")
        .header("Authorization", null)
        .httpMethod(HttpMethod.GET)
        .provide();

    Request authorizedRequest = E2ETest.mockRequest()
        .path("/")
        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("giorgio:password".getBytes()))
        .httpMethod(HttpMethod.GET)
        .provide();

    private static void assertUnauthorized(Response response) {

        assertEquals(
            UNAUTHORIZED,
            response.getHttpStatus()
        );

        assertEquals(
            "Basic",
            response.getHeaders().getFirst("WWW-Authenticate")
        );

    }

    private static void assertUnauthorizedRealm(Response response, String realm) {

        assertEquals(
            UNAUTHORIZED,
            response.getHttpStatus()
        );

        assertEquals(
            "Basic realm=\"" + realm + "\"",
            response.getHeaders().getFirst("WWW-Authenticate")
        );

    }

    @Test
    void testNoRealmNotAuthorized() {

        RequestHandler requestHandler = new BasicAuth(null).getRequestHandler();

        Response response = requestHandler.handle(unauthorizedRequest);

        assertUnauthorized(response);

    }

    @Test
    void testNotAuthorized() {

        RequestHandler requestHandler = new BasicAuth(realm).getRequestHandler();

        Response response = requestHandler.handle(unauthorizedRequest);

        assertUnauthorizedRealm(response, realm);

    }

    @Test
    void testAuthorizedNoRealm() {

        RequestHandler requestHandler = new BasicAuth(null).getRequestHandler();

        Response response = requestHandler.handle(authorizedRequest);

        assertEquals(OK, response.getHttpStatus());

    }

    @Test
    void testAuthorized() {

        RequestHandler requestHandler = new BasicAuth(realm).getRequestHandler();

        Response response = requestHandler.handle(authorizedRequest);

        assertEquals(OK, response.getHttpStatus());

    }

}
