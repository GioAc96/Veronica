package rocks.gioac96.veronica.auth;

import rocks.gioac96.veronica.core.PipelineBreakException;
import rocks.gioac96.veronica.core.Response;

/**
 * Breaks the pipeline on authentication failure.
 */
public class AuthenticationException extends PipelineBreakException {

    public AuthenticationException(Response response) {

        super(response);

    }

    public AuthenticationException(Throwable cause, Response response) {

        super(cause, response);

    }

}
