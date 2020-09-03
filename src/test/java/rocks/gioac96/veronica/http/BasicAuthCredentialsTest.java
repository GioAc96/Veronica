package rocks.gioac96.veronica.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Base64;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class BasicAuthCredentialsTest {

    private static String generateCredentials(String user, String password) {

        return Base64.getEncoder().encodeToString((user + ':' + password).getBytes());

    }

    private static String generateValidAuthHeader(String user, String password) {

        return " Basic " + generateCredentials(user, password);

    }

    @ParameterizedTest
    @CsvSource({
        "test, test",
        "user, password",
        "user, p:assword",
        "aladin, letmein",
        "WeiRdUsernamenciajsoifjaio183)(!£$/£(£!(/$#òàè\uD83D\uDC09, WeirderPassword+è+-....-:301934829\uD83D\uDC19",
    })
    public void testValidCredentials(String user, String password) throws BasicAuthCredentials.BasicAuthCredentialsParsingException {

        String authHeader = generateValidAuthHeader(user, password);

        BasicAuthCredentials parsedCredentials = BasicAuthCredentials.fromAuthorizationHeader(authHeader);

        assertEquals(
            user,
            parsedCredentials.getUser()
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
    public void testInvalidCredentials(String user, String password) throws BasicAuthCredentials.BasicAuthCredentialsParsingException {

        String authHeader = generateValidAuthHeader(user, password);

        BasicAuthCredentials parsedCredentials = BasicAuthCredentials.fromAuthorizationHeader(authHeader);

        assertNotEquals(
            user,
            parsedCredentials.getUser()
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
    public void testInvalidHeaders(String header) {

        assertThrows(
            BasicAuthCredentials.BasicAuthCredentialsParsingException.class,
            () -> BasicAuthCredentials.fromAuthorizationHeader(header)
        );

    }

}