package rocks.gioac96.veronica.tutorials.factories;

import rocks.gioac96.veronica.common.CommonRoutes;
import rocks.gioac96.veronica.core.Router;

public class MyRouter extends Router.RouterBuilder {

    @Override
    public void configure() {

        route(new RouteA());
        route(new RouteB());

        defaultRoute(CommonRoutes.notFound());

    }


}
