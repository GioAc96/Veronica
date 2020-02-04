package org.gioac96.veronica.routing.pipeline;

import lombok.RequiredArgsConstructor;
import org.gioac96.veronica.http.Response;

public class ResponseRenderingException extends PipelineBreakException {

    public ResponseRenderingException(Response response) {

        super(response);

    }

}
