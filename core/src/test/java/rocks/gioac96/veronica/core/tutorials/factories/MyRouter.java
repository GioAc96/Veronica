package rocks.gioac96.veronica.core.tutorials.factories;

import rocks.gioac96.veronica.validation.common.CommonRequestHandlers;
import rocks.gioac96.veronica.core.Router;

public class MyRouter extends Router.RouterBuilder  {

    @Override
    public void configure() {

        route(new RouteA());
        route(new RouteB());

        defaultRequestHandler(CommonRequestHandlers.notFound());

    }

}
