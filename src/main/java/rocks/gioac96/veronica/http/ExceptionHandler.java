package rocks.gioac96.veronica.http;

import java.io.IOException;

/**
 * Application exception handler.
 */
public interface ExceptionHandler {

    /**
     * Handles an exception and generates an appropriate response.
     *
     * @param e exception to handle
     * @return the generated response
     */
    default Response handle(Exception e) {

        return Response.builder()
            .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
            .emptyBody()
            .build();

    }

    /**
     * Handles internal server fatal exceptions.
     *
     * @param e exception to handle
     */
    default void handleExchangeException(IOException e) {

    }

}
