package rocks.gioac96.veronica.samples;

import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.common.CommonRoutes;
import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.core.Router;

public class NoFavIcon {

    public static void main(String[] args) {

        Application.builder()
            .router(
                Router.builder()
                    .route(CommonRoutes.noFavIcon())
                    .defaultRoute(Route.builder()
                        .requestHandler(request -> {

                            System.out.println(request.getPath().matches("/favicon.ico"));

                            return CommonResponses.ok();

                        })
                        .build())
                    .build()
            )
            .port(80)
            .build()
            .start();

    }

}
