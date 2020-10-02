package rocks.gioac96.veronica.common.routes;

import rocks.gioac96.veronica.common.CommonRequestHandlers;
import rocks.gioac96.veronica.common.CommonRequestMatchers;
import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;

public class NoFavicon extends Route.RouteBuilder implements BuildsSingleInstance {

    @Override
    protected void configure() {

        super.configure();

        requestHandler(CommonRequestHandlers.notFound());
        requestMatcher(CommonRequestMatchers.favicon());

    }

}
