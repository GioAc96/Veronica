package rocks.gioac96.veronica.http_basic_auth;

import java.util.Base64;
import rocks.gioac96.veronica.auth.Credentials;
import rocks.gioac96.veronica.core.Request;

/**
 * Utility class for http basic authentication.
 */
public final class BasicAuth {

    /**
     * Parses an http "Authorization" header to retrieve credentials in basic http authentication.
     *
     * @param headerValue value of the "Authorization" header
     * @return the parsed credentials
     * @throws BasicAuthCredentialsParsingException on parsing failure
     */
    public static Credentials fromAuthorizationHeader(String headerValue)
        throws BasicAuthCredentialsParsingException {

        try {

            String base64Credentials = headerValue.replaceFirst("^\\s*Basic\\s+", "");

            String decodedCredentials = new String(Base64.getDecoder().decode(base64Credentials));

            int firstColonPos = decodedCredentials.indexOf(':');

            return Credentials.builder()
                .username(decodedCredentials.substring(0, firstColonPos))
                .password(decodedCredentials.substring(firstColonPos + 1))
                .provide();

        } catch (Exception e) {

            throw new BasicAuthCredentialsParsingException(e);

        }

    }

    /**
     * Parses a request to retrieve credentials in basic http authentication.
     *
     * @param request request to parse
     * @return the parsed credentials
     * @throws BasicAuthCredentialsParsingException on parsing failure
     */
    public static Credentials fromRequest(Request request)
        throws BasicAuthCredentialsParsingException {

        return fromAuthorizationHeader(
            request.getHeaders().getFirst("Authorization")
        );

    }

    /**
     * Thrown when failing to parse basic authentication credentials.
     */
    public static final class BasicAuthCredentialsParsingException extends Exception {

        public BasicAuthCredentialsParsingException(Throwable cause) {
            super(cause);
        }
    }

}
