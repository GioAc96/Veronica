package rocks.gioac96.veronica.http.auth;

import lombok.Builder;
import lombok.NonNull;
import rocks.gioac96.veronica.http.CommonResponses;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.routing.pipeline.stages.PreFilter;

/**
 * Http basic authentication filter.
 */
@Builder
public class BasicAuthFilter implements PreFilter {

    @NonNull
    private final CredentialsChecker credentialsChecker;

    private final String realm;

    @Override
    public void filter(@NonNull Request request) {

        try {

            Credentials credentials = BasicAuth.fromRequest(request);

            if (!credentialsChecker.check(credentials)) {

                throw new AuthenticationFailedException(CommonResponses.promptBasicAuth(realm));

            }

        } catch (BasicAuth.BasicAuthCredentialsParsingException e) {

            throw new AuthenticationFailedException(CommonResponses.promptBasicAuth(realm));

        }

    }

}
