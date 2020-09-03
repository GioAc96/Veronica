package rocks.gioac96.veronica.http.auth;

/**
 * Checks credentials.
 */
public interface CredentialsChecker {

    boolean check(Credentials credentials);

}
