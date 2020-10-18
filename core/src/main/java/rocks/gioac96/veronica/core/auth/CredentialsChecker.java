package rocks.gioac96.veronica.core.auth;

/**
 * Checks credentials.
 */
public interface CredentialsChecker {

    boolean check(Credentials credentials);

}
