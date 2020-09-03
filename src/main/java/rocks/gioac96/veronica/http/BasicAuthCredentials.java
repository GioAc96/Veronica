package rocks.gioac96.veronica.http;

import java.util.Base64;
import lombok.Getter;
import lombok.NonNull;

@Getter
public final class BasicAuthCredentials {

    public static final class BasicAuthCredentialsParsingException extends Exception {

        public BasicAuthCredentialsParsingException(Throwable cause) {
            super(cause);
        }
    }

    private final String user;
    private final String password;

    private BasicAuthCredentials(
        @NonNull String user,
        @NonNull String password
    ) {

        this.user = user;
        this.password = password;

    }


    public static BasicAuthCredentials fromAuthorizationHeader(String headerValue)
        throws BasicAuthCredentialsParsingException {

        try {

            String base64Credentials = headerValue.replaceFirst("^\\s+Basic\\s+", "");

            String decodedCredentials = new String(Base64.getDecoder().decode(base64Credentials));

            int firstColonPos = decodedCredentials.indexOf(':');

            return new BasicAuthCredentials(
                decodedCredentials.substring(0, firstColonPos),
                decodedCredentials.substring(firstColonPos + 1)
            );

        } catch (Exception e) {

            throw new BasicAuthCredentialsParsingException(e);

        }

    }

}
