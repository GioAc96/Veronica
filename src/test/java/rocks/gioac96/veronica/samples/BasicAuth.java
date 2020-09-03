package rocks.gioac96.veronica.samples;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.auth.http_basic.BasicAuthFilterBuilder;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.core.Pipeline;

public class BasicAuth {

    public static void main(String[] args) {

        Application.builder()
            .port(80)
            .router(Router.builder()
                .fallbackRoute(Route.builder()
                    .pipeline(Pipeline.builder()
                        .preFilter(new BasicAuthFilterBuilder()
                            .realm("My website")
                            .credentialsChecker(credentials -> credentials.getUsername().equals("giorgio") && credentials.getPassword().equals("password"))
                            .build())
                        .build())
                    .requestHandler(request -> CommonResponses.ok("You are authenticated"))
                    .build())
                .build())
            .build()
            .start();

    }

}
