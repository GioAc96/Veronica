package rocks.gioac96.veronica.http;

import lombok.experimental.UtilityClass;

/**
 * Utility class for instantiating common Response objects.
 */
@UtilityClass
public class CommonResponses {

    /**
     * Instantiates a response with a blank body.
     * @param httpStatus the http status of the response
     * @return the instantiated response
     */
    public Response empty(HttpStatus httpStatus) {

        return Response.builder()
            .httpStatus(httpStatus)
            .emptyBody()
            .build();

    }

    /**
     * Instantiates an http "OK" response with an empty body.
     * @return the instantiated response
     */
    public Response ok() {

        return empty(HttpStatus.OK);

    }

    /**
     * Instantiates an http "NOT FOUND" response with an empty body.
     * @return the instantiated response
     */
    public Response notFound() {

        return empty(HttpStatus.NOT_FOUND);

    }


    /**
     * Instantiates an http "INTERNAL SERVER ERROR" response with an empty body.
     * @return the instantiated response
     */
    public Response internalError() {

        return empty(HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
