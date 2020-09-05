package rocks.gioac96.veronica.common.routes;

import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.core.Route;

public class NotFound extends Route.RouteBuilder {

    @Override
    protected void configure() {

        requestHandler(request -> CommonResponses.notFound());

    }

}
