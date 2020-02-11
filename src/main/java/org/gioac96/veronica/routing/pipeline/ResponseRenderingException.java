package org.gioac96.veronica.routing.pipeline;

import lombok.Builder;
import lombok.NonNull;
import org.gioac96.veronica.http.Response;

/**
 * Pipeline exception thrown by the {@link ResponseRenderer}.
 */

public final class ResponseRenderingException extends PipelineBreakException {

    @Builder
    public ResponseRenderingException(@NonNull Response response) {
        super(response);
    }

}
