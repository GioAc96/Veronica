package rocks.gioac96.veronica.auth;

/**
 * Checks credentials.
 */
public interface CredentialsChecker {

    boolean check(Credentials credentials);

}
