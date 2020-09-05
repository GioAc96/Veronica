package rocks.gioac96.veronica.common.routes;

import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;

public class RedirectToSecure extends Route.RouteBuilder implements BuildsSingleInstance {

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
