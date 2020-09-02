package rocks.gioac96.veronica.routing;

import lombok.experimental.UtilityClass;
import rocks.gioac96.veronica.http.CommonResponses;
import rocks.gioac96.veronica.http.HttpStatus;
import rocks.gioac96.veronica.http.Response;

/**
 * Helper class for common routes.
 */
@UtilityClass
public class CommonRoutes {

    private  Route empty(HttpStatus httpStatus) {

        return Route.builder()
            .alwaysMatch()
            .requestHandler(request -> CommonResponses.empty(httpStatus))
            .build();

    }

    /**
     * Instantiates a {@link Route} that redirects http requests to https.
     *
     * @return the instantiated route
     */
    public  Route redirectToSecure() {

        return Route.builder()
            .requestMatcher(request -> !request.isSecure())
            .requestHandler(request -> Response.builder()
                .httpStatus(HttpStatus.MOVED_PERMANENTLY)
                .header("Location", request.getUri().toString().replaceFirst("^http", "https"))
                .emptyBody()
                .build()
            )
            .build();

    }

    /**
     * Instantiates a {@link Route} that always returns an http "OK" response.
     *
     * @return the instantiated route
     */
    public  Route ok() {

        return empty(HttpStatus.OK);

    }

    /**
     * Instantiates a {@link Route} that always returns an http "NOT FOUND" response.
     *
     * @return the instantiated route
     */
    public  Route notFound() {

        return empty(HttpStatus.NOT_FOUND);

    }

}
