package rocks.gioac96.veronica.common.routes;

import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.core.Route;

public class RedirectToSecure extends Route.RouteBuilder {

    @Override
    protected void configure() {

        requestMatcher(request -> !request.isSecure());

        requestHandler(request ->
            CommonResponses.permanentRedirect(
                request.getUri().toString().replaceFirst("^http", "https")
            )
        );

    }

}
