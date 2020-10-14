package rocks.gioac96.veronica.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * Authentication credentials.
 */
@Getter
@NonNull
@AllArgsConstructor
public class Credentials {

    private final String username;
    private final String password;

}
