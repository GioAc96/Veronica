package rocks.gioac96.veronica.common.routes;

import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;

public class NotFound extends Route.RouteBuilder implements BuildsSingleInstance {

    @Override
    protected void configure() {

        requestHandler(request -> CommonResponses.notFound());

    }

}
