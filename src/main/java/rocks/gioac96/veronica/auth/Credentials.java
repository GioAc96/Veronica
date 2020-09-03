package rocks.gioac96.veronica.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * Class for holding authentication credentials.
 */
@Getter
@NonNull
@AllArgsConstructor
public class Credentials {

    private final String username;
    private final String password;

}
