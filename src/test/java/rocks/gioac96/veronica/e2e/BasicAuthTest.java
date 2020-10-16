package rocks.gioac96.veronica.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static rocks.gioac96.veronica.core.HttpStatus.OK;
import static rocks.gioac96.veronica.core.HttpStatus.UNAUTHORIZED;

import java.util.Base64;
import org.junit.jupiter.api.Test;
import rocks.gioac96.veronica.core.HttpMethod;
import rocks.gioac96.veronica.core.Request;
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

        Router router = new BasicAuth(null).getRouter();

        Response response = router.handle(unauthorizedRequest);

        assertUnauthorized(response);

    }

    @Test
    void testNotAuthorized() {

        Router router = new BasicAuth(realm).getRouter();

        Response response = router.handle(unauthorizedRequest);

        assertUnauthorizedRealm(response, realm);

    }

    @Test
    void testAuthorizedNoRealm() {

        Router router = new BasicAuth(null).getRouter();

        Response response = router.handle(authorizedRequest);

        assertEquals(OK, response.getHttpStatus());

    }

    @Test
    void testAuthorized() {

        Router router = new BasicAuth(realm).getRouter();

        Response response = router.handle(authorizedRequest);

        assertEquals(OK, response.getHttpStatus());

    }

}
