package rocks.gioac96.veronica.auth;

public interface HoldsAuthenticationData {

    void setBasicAuthenticationResult(Credentials credentials, boolean isAuthenticated);

}
