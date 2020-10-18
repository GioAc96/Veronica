package rocks.gioac96.veronica.auth;

import rocks.gioac96.veronica.auth.Credentials;

public interface HoldsAuthenticationData {

    void setBasicAuthenticationResult(Credentials credentials, boolean isAuthenticated);

}
