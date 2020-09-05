package rocks.gioac96.veronica.common.responses;

import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;

public class PromptBasicAuthRealm extends Response.ResponseBuilder implements BuildsMultipleInstances {

    protected String realm = "";

    public PromptBasicAuthRealm realm(String realm) {

        this.realm = realm;

        return this;

    }

    @Override
    protected void configure() {

        httpStatus(HttpStatus.UNAUTHORIZED);
        header("WWW-Authenticate", "Basic realm=\"" + realm + "\"");

    }

}
