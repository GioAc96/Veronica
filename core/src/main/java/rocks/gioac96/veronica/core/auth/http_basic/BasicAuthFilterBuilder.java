package rocks.gioac96.veronica.core.auth.http_basic;

import lombok.NonNull;
import rocks.gioac96.veronica.core.auth.Credentials;
import rocks.gioac96.veronica.core.auth.CredentialsChecker;
import rocks.gioac96.veronica.core.auth.HoldsAuthenticationData;
import rocks.gioac96.veronica.core.common.CommonResponses;
import rocks.gioac96.veronica.core.providers.Provider;
import rocks.gioac96.veronica.core.pipeline.PipelineStage;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;

/**
 * Http basic authentication filter builder.
 */
public class BasicAuthFilterBuilder<D extends HoldsAuthenticationData> extends ConfigurableProvider<PipelineStage<D>> {

    protected CredentialsChecker credentialsChecker;
    protected String realm;

    public BasicAuthFilterBuilder<D> credentialsChecker(@NonNull CredentialsChecker credentialsChecker) {

        this.credentialsChecker = credentialsChecker;
        return this;

    }

    public BasicAuthFilterBuilder<D> credentialsChecker(@NonNull Provider<CredentialsChecker> credentialsCheckerProvider) {

        return credentialsChecker(credentialsCheckerProvider.provide());

    }

    public BasicAuthFilterBuilder<D> realm(String realm) {

        this.realm = realm;
        return this;

    }

    public BasicAuthFilterBuilder<D> realm(@NonNull Provider<String> realm) {

        return realm(realm.provide());

    }

    @Override
    protected boolean isValid() {

        return credentialsChecker != null;

    }

    @Override
    protected PipelineStage<D> instantiate() {

        return (request, responseBuilder, data) -> {

            try {

                Credentials credentials = BasicAuth.fromRequest(request);

                if (credentialsChecker.check(credentials)) {

                    data.setBasicAuthenticationResult(credentials, true);

                } else {

                    data.setBasicAuthenticationResult(credentials, false);

                    return CommonResponses.promptBasicAuth(realm);

                }

            } catch (BasicAuth.BasicAuthCredentialsParsingException e) {

                data.setBasicAuthenticationResult(null, false);

                return CommonResponses.promptBasicAuth(realm);

            }

            return null;

        };
    }

}
