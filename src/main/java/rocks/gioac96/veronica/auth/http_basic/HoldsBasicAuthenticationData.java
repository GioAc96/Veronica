package rocks.gioac96.veronica.auth.http_basic;

import rocks.gioac96.veronica.auth.Credentials;

public interface HoldsBasicAuthenticationData {

    void setBasicAuthenticationResult(Credentials credentials, boolean isAuthenticated);

}
