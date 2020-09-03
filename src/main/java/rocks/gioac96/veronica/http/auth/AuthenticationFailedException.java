package rocks.gioac96.veronica.http.auth;

import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.pipeline.stages.PipelineBreakException;

/**
 * Breaks the pipeline on authentication failure.
 */
public class AuthenticationFailedException extends PipelineBreakException {

    public AuthenticationFailedException(Response response) {

        super(response);

    }

    public AuthenticationFailedException(Throwable cause, Response response) {

        super(cause, response);

    }

}
