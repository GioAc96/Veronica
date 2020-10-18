package rocks.gioac96.veronica.core.auth;

public interface HoldsAuthenticationData {

    void setBasicAuthenticationResult(Credentials credentials, boolean isAuthenticated);

}
