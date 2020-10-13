package rocks.gioac96.veronica.auth.http_basic;

import lombok.NonNull;
import rocks.gioac96.veronica.auth.AuthenticationException;
import rocks.gioac96.veronica.auth.Credentials;
import rocks.gioac96.veronica.auth.CredentialsChecker;
import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.core.PreFilter;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.Provider;

/**
 * Http basic authentication filter builder.
 */
@SuppressWarnings("checkstyle:MissingJavadocMethod")
public class BasicAuthFilterBuilder extends Builder<PreFilter> implements BuildsMultipleInstances {

    private CredentialsChecker credentialsChecker;
    private String realm;

    public BasicAuthFilterBuilder credentialsChecker(@NonNull CredentialsChecker credentialsChecker) {

        this.credentialsChecker = credentialsChecker;
        return this;

    }

    public BasicAuthFilterBuilder credentialsChecker(@NonNull Provider<CredentialsChecker> credentialsCheckerProvider) {

        return credentialsChecker(credentialsCheckerProvider.provide());

    }

    public BasicAuthFilterBuilder realm(String realm) {

        this.realm = realm;
        return this;

    }

    public BasicAuthFilterBuilder realm(Provider<String> realmProvider) {

        return realm(realmProvider.provide());

    }

    @Override
    protected boolean isValid() {

        return super.isValid()
            && credentialsChecker != null;

    }

    @Override
    protected PreFilter instantiate() {

        return request -> {

            try {

                Credentials credentials = BasicAuth.fromRequest(request);

                if (!credentialsChecker.check(credentials)) {

                    throw new AuthenticationException(CommonResponses.promptBasicAuth(realm));

                }

            } catch (BasicAuth.BasicAuthCredentialsParsingException e) {

                throw new AuthenticationException(CommonResponses.promptBasicAuth(realm));

            }

        };
    }

}
