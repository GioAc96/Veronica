package rocks.gioac96.veronica.samples;

import lombok.Getter;
import rocks.gioac96.veronica.auth.http_basic.BasicAuthFilterBuilder;
import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.Pipeline;
import rocks.gioac96.veronica.core.PreFilter;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Router;

public class BasicAuth {

    @Getter
    private final Router router;

    public BasicAuth(String realm) {

        PreFilter basicAuthFilter;

        if (realm == null) {

            basicAuthFilter = new BasicAuthFilterBuilder()
                .credentialsChecker(credentials ->
                    credentials.getUsername().equals("giorgio")
                        && credentials.getPassword().equals("password")
                )
                .build();

        } else {

            basicAuthFilter = new BasicAuthFilterBuilder()
                .realm(realm)
                .credentialsChecker(credentials ->
                    credentials.getUsername().equals("giorgio")
                        && credentials.getPassword().equals("password")
                )
                .build();

        }

        router = Router.builder()
            .defaultRequestHandler(Pipeline.builder()
                .preFilter(basicAuthFilter)
                .requestHandler(request -> Response.builder().body("You are authenticated").build())
                .build())
            .build();
    }

    public static void main(String[] args) {

        Application.builder()
            .port(80)
            .router(new BasicAuth("my realm").router)
            .build()
            .start();

    }

}
