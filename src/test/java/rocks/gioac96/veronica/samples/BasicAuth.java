package rocks.gioac96.veronica.samples;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.http.CommonResponses;
import rocks.gioac96.veronica.http.auth.BasicAuthFilter;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;
import rocks.gioac96.veronica.routing.pipeline.Pipeline;

public class BasicAuth {

    public static void main(String[] args) {

        Application.builder()
            .port(80)
            .router(Router.builder()
                .fallbackRoute(Route.builder()
                    .pipeline(Pipeline.builder()
                        .preFilter(BasicAuthFilter.builder()
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
