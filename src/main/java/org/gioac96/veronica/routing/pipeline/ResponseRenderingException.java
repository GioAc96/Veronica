package org.gioac96.veronica.routing.pipeline;

import org.gioac96.veronica.http.Response;

/**
 * Pipeline exception thrown by the {@link ResponseRenderer}.
 */
public class ResponseRenderingException extends PipelineBreakException {

    public ResponseRenderingException(Response response) {

        super(response);

    }

}
