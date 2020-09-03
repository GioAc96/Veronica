package rocks.gioac96.veronica.auth.http_basic;

import lombok.NonNull;
import rocks.gioac96.veronica.auth.AuthenticationException;
import rocks.gioac96.veronica.auth.Credentials;
import rocks.gioac96.veronica.auth.CredentialsChecker;
import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.core.PreFilter;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.Provider;

/**
 * Http basic authentication filter.
 */
public class BasicAuthFilter implements PreFilter {

    @NonNull
    private final CredentialsChecker credentialsChecker;

    private final String realm;

    protected BasicAuthFilter(BasicAuthFilterBuilder b) {
        this.credentialsChecker = b.credentialsChecker;
        this.realm = b.realm;
    }

    public static BasicAuthFilterBuilder builder() {
        return new BasicAuthFilterBuilder() {
        };
    }

    @Override
    public void filter(@NonNull Request request) {

        try {

            Credentials credentials = BasicAuth.fromRequest(request);

            if (!credentialsChecker.check(credentials)) {

                throw new AuthenticationException(CommonResponses.promptBasicAuth(realm));

            }

        } catch (BasicAuth.BasicAuthCredentialsParsingException e) {

            throw new AuthenticationException(CommonResponses.promptBasicAuth(realm));

        }

    }

    
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public static class BasicAuthFilterBuilder extends Builder<BasicAuthFilter> {

        private @NonNull CredentialsChecker credentialsChecker;
        private String realm;

        public BasicAuthFilterBuilder credentialsChecker(
            @NonNull Provider<CredentialsChecker> credentialsCheckerFactory
        ) {

            return credentialsChecker(credentialsCheckerFactory.provide());

        }

        public BasicAuthFilterBuilder credentialsChecker(@NonNull CredentialsChecker credentialsChecker) {

            this.credentialsChecker = credentialsChecker;
            return this;

        }

        public BasicAuthFilterBuilder realm(@NonNull Provider<String> realmFactory) {

            return realm(realmFactory.provide());

        }

        public BasicAuthFilterBuilder realm(String realm) {

            this.realm = realm;
            return this;

        }

        @Override
        protected BasicAuthFilter instantiate() {
            return new BasicAuthFilter(this);
        }

    }
    
}
