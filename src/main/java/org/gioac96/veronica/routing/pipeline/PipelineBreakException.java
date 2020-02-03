package org.gioac96.veronica.routing.pipeline;

import lombok.Getter;
import org.gioac96.veronica.http.Response;

/**
 * {@link Pipeline} error exception. It is thrown by any of the stages in a {@link Pipeline} that breaks the
 * execution of the pipeline itself.
 */
public class PipelineBreakException extends Exception {

    @Getter
    private final Response response;

    public PipelineBreakException(Response response) {

        this.response = response;

    }

}
