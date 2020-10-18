package rocks.gioac96.veronica.tutorials.factories;

import rocks.gioac96.veronica.common.CommonRequestHandlers;
import rocks.gioac96.veronica.Router;

public class MyRouter extends Router.RouterBuilder  {

    @Override
    public void configure() {

        route(new RouteA());
        route(new RouteB());

        defaultRequestHandler(CommonRequestHandlers.notFound());

    }

}
