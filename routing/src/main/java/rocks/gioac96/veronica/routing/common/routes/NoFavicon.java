package rocks.gioac96.veronica.routing.common.routes;

import rocks.gioac96.veronica.core.common.CommonRequestHandlers;
import rocks.gioac96.veronica.routing.common.CommonRequestMatchers;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.core.providers.Singleton;

public class NoFavicon
    extends Route.RouteBuilder
    implements Singleton {

    @Override
    protected void configure() {

        requestHandler(CommonRequestHandlers.notFound());
        requestMatcher(CommonRequestMatchers.favicon());

        super.configure();

    }

}
