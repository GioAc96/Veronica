package org.gioac96.veronica.routing.pipeline;

import lombok.Getter;
import org.gioac96.veronica.http.Response;

public class PipelineBreakException extends Exception {

    @Getter
    private final Response response;

    public PipelineBreakException(Response response) {

        this.response = response;

    }

}
