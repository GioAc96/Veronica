package rocks.gioac96.veronica.routing;

import lombok.experimental.UtilityClass;
import rocks.gioac96.veronica.http.HttpStatus;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.pipeline.stages.RequestHandlerPayload;

/**
 * Helper class for common routes.
 */
@UtilityClass
public class CommonRoutes {

    /**
     * Instantiates a {@link Route} that redirects http requests to https.
     *
     * @return the instantiated route
     */
    public Route<? extends Request, ? extends Response> redirectToSecure() {

        return Route.builder()
            .requestMatcher(request -> !request.isSecure())
            .requestHandler(request -> RequestHandlerPayload.ok(Response.builder()
                .httpStatus(HttpStatus.MOVED_PERMANENTLY)
                .header("Location", request.getUri().toString().replaceFirst("^http", "https"))
                .body("")
                .build())
            )
            .build();

    }

}
