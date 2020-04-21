package rocks.gioac96.veronica.tutorials;

import static rocks.gioac96.veronica.routing.matching.CommonRequestMatchers.get;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Map;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.http.SetCookieHeader;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;
import rocks.gioac96.veronica.routing.matching.RequestMatcher;

public class Cookies {

    public static void main(String[] args) {

        Route<Request, Response> route = Route.builder()
            .requestMatcher(get("/"))
            .requestHandler(req -> {

                Map<String, String> cookie = req.getCookie();

                int hitCounter = 0;

                if (cookie.containsKey("hit-counter")) {

                    hitCounter = Integer.parseInt(cookie.get("hit-counter"));

                }

                return Response.builder()
                    .cookie(SetCookieHeader.builder()
                        .name("hit-counter")
                        .value(String.valueOf(hitCounter + 1))
                        .expires(ZonedDateTime.now().plusSeconds(1))
                        .build()
                    )
                    .body("You have visited this page " + String.valueOf(hitCounter) + " times before")
                    .build();

            })
            .build();

        Router<Request, Response> router = Router.builder()
            .route(route)
            .fallbackRoute(Route.builder().requestHandler(request -> Response.builder().body("").build()).build())
            .build();

        int port = 8000;

        try {

            Application<Request, Response> app = Application.basic(port, router);
            app.start();

        } catch (IOException e) {

            System.out.println("Unable to start the application: " + e.getMessage());

        }


    }


}
