package rocks.gioac96.veronica.samples;

import lombok.Getter;
import rocks.gioac96.veronica.auth.Credentials;
import rocks.gioac96.veronica.auth.CredentialsChecker;
import rocks.gioac96.veronica.auth.HoldsAuthenticationData;
import rocks.gioac96.veronica.http_basic_auth.BasicAuthFilterBuilder;
import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.core.pipeline.Pipeline;

public class BasicAuth {

    @Getter
    private final RequestHandler requestHandler;

    public BasicAuth(String realm) {

        CredentialsChecker credentialsChecker = credentials ->
            credentials.getUsername().equals("giorgio")
                && credentials.getPassword().equals("password");

        BasicAuthFilterBuilder<PipelineData> basicAuthFilterBuilder = new BasicAuthFilterBuilder<PipelineData>()
            .credentialsChecker(credentialsChecker)
            .realm(realm);

        if (realm != null) {

            basicAuthFilterBuilder.realm(realm);

        }

        requestHandler = Pipeline.<PipelineData>builder()
            .dataFactory(request -> new PipelineData())
            .stage(basicAuthFilterBuilder.provide())
            .stage((request, responseBuilder, data) -> {

                if (data.getCredentials() == null) {

                    return responseBuilder.body("no credentials supplied").provide();

                }

                return null;

            })
            .stage((request, responseBuilder, data) -> {

                if (! data.isAuthenticated()) {

                    return responseBuilder.body("invalid credentials").provide();

                }

                return null;

            })
            .stage((request, responseBuilder, data) -> {

                responseBuilder.body("You are accessing the restricted area as user: " + data.getCredentials().getUsername());

                return null;

            })
            .provide();
    }

    public static void main(String[] args) {

        Application.builder()
            .port(80)
            .requestHandler(new BasicAuth("my realm").requestHandler)
            .provide()
            .start();

    }

    @Getter
    private static final class PipelineData implements HoldsAuthenticationData {

        private Credentials credentials;
        private boolean isAuthenticated;

        @Override
        public void setBasicAuthenticationResult(Credentials credentials, boolean isAuthenticated) {

            this.credentials = credentials;
            this.isAuthenticated = isAuthenticated;

        }

    }

}
