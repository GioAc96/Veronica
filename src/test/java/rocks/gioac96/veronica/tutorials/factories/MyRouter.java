package rocks.gioac96.veronica.tutorials.factories;

import rocks.gioac96.veronica.common.CommonRoutes;
import rocks.gioac96.veronica.core.RouterFactory;

public class MyRouter extends RouterFactory {

    @Override
    public void configure() {

        route(new RouteA());
        route(new RouteB());

        fallbackRoute(CommonRoutes.notFound());

    }


}
