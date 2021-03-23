package rocks.gioac96.veronica.auth;

public interface HoldsAuthenticationData {

    void setAuthenticationResult(Credentials credentials, boolean isAuthenticated);

}
