package rocks.gioac96.veronica.validation.common.responses;

import lombok.NonNull;
import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.providers.Provider;

public class PromptBasicAuthRealm extends Response.ResponseBuilder {

    protected String realm;

    public PromptBasicAuthRealm realm(@NonNull String realm) {

        this.realm = realm;

        return this;

    }

    public PromptBasicAuthRealm realm(@NonNull Provider<String> realmProvider) {

        return this.realm(realmProvider.provide());

    }

    @Override
    protected boolean isValid() {

        return super.isValid()
            && realm != null;

    }

    @Override
    protected void configure() {

        httpStatus(HttpStatus.UNAUTHORIZED);
        header("WWW-Authenticate", "Basic realm=\"" + realm + "\"");

        super.configure();

    }

}
