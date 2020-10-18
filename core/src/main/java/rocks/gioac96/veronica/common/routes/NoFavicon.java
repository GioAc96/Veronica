package rocks.gioac96.veronica.common.routes;

import rocks.gioac96.veronica.common.CommonRequestHandlers;
import rocks.gioac96.veronica.common.CommonRequestMatchers;
import rocks.gioac96.veronica.Route;
import rocks.gioac96.veronica.providers.Singleton;

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
