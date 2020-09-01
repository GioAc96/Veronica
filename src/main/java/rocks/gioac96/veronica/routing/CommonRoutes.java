package rocks.gioac96.veronica.routing;

import lombok.experimental.UtilityClass;
import rocks.gioac96.veronica.http.CommonResponses;
import rocks.gioac96.veronica.http.HttpStatus;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.pipeline.stages.RequestHandlerPayload;

/**
 * Helper class for common routes.
 */
@UtilityClass
public class CommonRoutes {

    private <Q extends Request> Route<Q, Response> empty(HttpStatus httpStatus) {

        return Route.<Q, Response>builder()
            .alwaysMatch()
            .requestHandler(request -> RequestHandlerPayload.ok(CommonResponses.empty(httpStatus)))
            .build();

    }

    /**
     * Instantiates a {@link Route} that redirects http requests to https.
     *
     * @param <Q> type of the request
     * @return the instantiated route
     */
    public <Q extends Request> Route<Q, Response> redirectToSecure() {

        return Route.<Q, Response>builder()
            .requestMatcher(request -> !request.isSecure())
            .requestHandler(request -> RequestHandlerPayload.ok(Response.builder()
                .httpStatus(HttpStatus.MOVED_PERMANENTLY)
                .header("Location", request.getUri().toString().replaceFirst("^http", "https"))
                .emptyBody()
                .build())
            )
            .build();

    }

    /**
     * Instantiates a {@link Route} that always returns an http "OK" response.
     *
     * @param <Q> type of the request
     * @return the instantiated route
     */
    public <Q extends Request> Route<Q, Response> ok() {

        return empty(HttpStatus.OK);

    }

    /**
     * Instantiates a {@link Route} that always returns an http "NOT FOUND" response.
     *
     * @param <Q> type of the request
     * @return the instantiated route
     */
    public <Q extends Request> Route<Q, Response> notFound() {

        return empty(HttpStatus.NOT_FOUND);

    }

}
