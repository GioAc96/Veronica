package rocks.gioac96.veronica.tutorials;

import static rocks.gioac96.veronica.common.CommonRequestMatchers.favicon;

import java.util.Map;
import rocks.gioac96.veronica.common.CommonRoutes;
import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.core.SetCookieHeader;
import rocks.gioac96.veronica.providers.CreationException;

public class Cookies {

    public static void main(String[] args) {

        Router router = Router.builder()
            .route(CommonRoutes.noFavIcon())
            .defaultRequestHandler(req -> {

                Map<String, String> cookie = req.getCookie();

                int hitCounter = 0;

                if (cookie.containsKey("hit-counter")) {

                    hitCounter = Integer.parseInt(cookie.get("hit-counter"));

                }

                return Response.builder()
                    .cookie(SetCookieHeader.builder()
                        .name("hit-counter")
                        .value(String.valueOf(hitCounter + 1))
                        .build()
                    )
                    .body("You have visited this page " + hitCounter + " times before")
                    .build();

            })
            .build();

        int port = 8000;

        try {

            Application app = Application.builder()
                .port(port)
                .router(router)
                .build();
            app.start();

        } catch (CreationException e) {

            System.out.println("Unable to start the application: " + e.getMessage());

        }


    }


}
