package rocks.gioac96.veronica.auth.http_basic;

import lombok.NonNull;
import rocks.gioac96.veronica.auth.AuthenticationException;
import rocks.gioac96.veronica.auth.Credentials;
import rocks.gioac96.veronica.auth.CredentialsChecker;
import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.core.PreFilter;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.factories.Factory;

/**
 * Http basic authentication filter.
 */
public class BasicAuthFilter implements PreFilter {

    @NonNull
    private final CredentialsChecker credentialsChecker;

    private final String realm;

    protected BasicAuthFilter(BasicAuthFilterBuilder<?, ?> b) {
        this.credentialsChecker = b.credentialsChecker;
        this.realm = b.realm;
    }

    public static BasicAuthFilterBuilder<?, ?> builder() {
        return new BasicAuthFilterBuilderImpl();
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

    public abstract static class BasicAuthFilterBuilder<
        C extends BasicAuthFilter,
        B extends BasicAuthFilterBuilder<C, B>
    > implements Factory<BasicAuthFilter> {

        private @NonNull CredentialsChecker credentialsChecker;
        private String realm;

        public B credentialsChecker(@NonNull Factory<CredentialsChecker> credentialsCheckerFactory) {

            return credentialsChecker(credentialsCheckerFactory.build());

        }

        public B credentialsChecker(@NonNull CredentialsChecker credentialsChecker) {

            this.credentialsChecker = credentialsChecker;
            return self();

        }

        public B realm(@NonNull Factory<String> realmFactory) {

            return realm(realmFactory.build());

        }

        public B realm(String realm) {

            this.realm = realm;
            return self();

        }

        protected abstract B self();

        public abstract C build();

    }

    private static final class BasicAuthFilterBuilderImpl extends BasicAuthFilterBuilder<BasicAuthFilter, BasicAuthFilterBuilderImpl> {
        private BasicAuthFilterBuilderImpl() {
        }

        protected BasicAuthFilter.BasicAuthFilterBuilderImpl self() {
            return this;
        }

        public BasicAuthFilter build() {
            return new BasicAuthFilter(this);
        }
    }
}
