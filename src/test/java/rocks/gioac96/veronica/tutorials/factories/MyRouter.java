package rocks.gioac96.veronica.tutorials.factories;

import rocks.gioac96.veronica.common.CommonRequestHandlers;
import rocks.gioac96.veronica.common.CommonRoutes;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;

public class MyRouter extends Router.RouterBuilder implements BuildsSingleInstance {

    @Override
    public void configure() {

        route(new RouteA());
        route(new RouteB());

        defaultRequestHandler(CommonRequestHandlers.notFound());

    }

}
