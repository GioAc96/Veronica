package rocks.gioac96.veronica.samples;

import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.auth.http_basic.BasicAuthFilterBuilder;
import rocks.gioac96.veronica.core.Pipeline;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Router;

public class BasicAuth {

    public static void main(String[] args) {

        Application.builder()
            .port(80)
            .router(Router.builder()
                .defaultRequestHandler(Pipeline.builder()
                    .preFilter(new BasicAuthFilterBuilder()
                        .realm("My realm")
                        .credentialsChecker(credentials -> credentials.getUsername().equals("giorgio") && credentials.getPassword().equals("password"))
                        .build())
                    .requestHandler(request -> Response.builder().body("You are authenticated").build())
                    .build())
                .build())
            .build();

    }

}
