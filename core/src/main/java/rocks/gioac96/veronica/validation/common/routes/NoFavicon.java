package rocks.gioac96.veronica.validation.common.routes;

import rocks.gioac96.veronica.validation.common.CommonRequestHandlers;
import rocks.gioac96.veronica.validation.common.CommonRequestMatchers;
import rocks.gioac96.veronica.core.Route;
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
