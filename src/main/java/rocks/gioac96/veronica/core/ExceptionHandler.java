package rocks.gioac96.veronica.core;

import java.io.IOException;
import rocks.gioac96.veronica.common.CommonResponses;

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

        e.printStackTrace();

        return CommonResponses.internalError();

    }

    /**
     * Handles internal server fatal exceptions.
     *
     * @param e exception to handle
     */
    default void handleExchangeException(IOException e) {

        e.printStackTrace();

    }

}
