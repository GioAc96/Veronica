package rocks.gioac96.veronica.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import rocks.gioac96.veronica.auth.Credentials;
import rocks.gioac96.veronica.auth.http_basic.BasicAuth;

class BasicAuthTest {

    private static String generateCredentials(String user, String password) {

        return Base64.getEncoder().encodeToString((user + ':' + password).getBytes());

    }

    private static String generateValidAuthHeader(String user, String password) {

        return "Basic " + generateCredentials(user, password);

    }

    @ParameterizedTest
    @CsvSource({
        "test, test",
        "user, password",
        "user, p:assword",
        "aladin, letmein",
        "WeiRdUsernamenciajsoifjaio183)(!£$/£(£!(/$#òàè, WeirderPassword+è+-.-:301934829",
    })
    public void testValidCredentials(String user, String password) throws BasicAuth.BasicAuthCredentialsParsingException {

        String authHeader = generateValidAuthHeader(user, password);

        Credentials parsedCredentials = BasicAuth.fromAuthorizationHeader(authHeader);

        assertEquals(
            user,
            parsedCredentials.getUsername()
        );
        assertEquals(
            password,
            parsedCredentials.getPassword()
        );

    }

    @ParameterizedTest
    @CsvSource({
        ":, test",
        "user:, test",
        ":user, test",
    })
    public void testInvalidCredentials(String user, String password) throws BasicAuth.BasicAuthCredentialsParsingException {

        String authHeader = generateValidAuthHeader(user, password);

        Credentials parsedCredentials = BasicAuth.fromAuthorizationHeader(authHeader);

        assertNotEquals(
            user,
            parsedCredentials.getUsername()
        );
        assertNotEquals(
            password,
            parsedCredentials.getPassword()
        );


    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        " ",
        "some random text",
        " Basic someinvalidbase64"
    })
    public void testInvalidHeader(String header) {

        assertThrows(
            BasicAuth.BasicAuthCredentialsParsingException.class,
            () -> BasicAuth.fromAuthorizationHeader(header)
        );

    }

    @Test
    public void testNullHeader() {

        assertThrows(
            BasicAuth.BasicAuthCredentialsParsingException.class,
            () -> BasicAuth.fromAuthorizationHeader(null)
        );

    }

}