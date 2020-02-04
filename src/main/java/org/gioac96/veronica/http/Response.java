package org.gioac96.veronica.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.gioac96.veronica.routing.pipeline.PipelineBreakException;
import org.gioac96.veronica.routing.pipeline.ResponseRenderer;
import org.gioac96.veronica.routing.pipeline.ResponseRenderingException;

/**
 * Http response.
 */
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Response {

    @Getter
    @Setter
    @NonNull
    protected HttpStatus httpStatus;

    @Getter
    @Builder.Default
    private String body = null;

    /**
     * Checks whether the response is already rendered.
     *
     * @return true iff the response is already rendered
     */
    public boolean isRendered() {

        return body != null;

    }

    /**
     * Renders the response with the specified renderer, if the response is not already rendered.
     *
     * @param responseRenderer renderer to use to rendered the response
     * @return true iff the response was not already rendered
     * @throws ResponseRenderingException on rendering failure
     */
    public boolean render(@NonNull ResponseRenderer responseRenderer) throws ResponseRenderingException {

        /*
         * Not calling the setBody method to avoid rendering the response with the responseRenderer
         * if it is already rendered.
         */

        if (isRendered()) {

            return false;

        } else {

            this.body = responseRenderer.render(this);

            return true;

        }

    }

    /**
     * Sets the body of the response if the response is not already rendered.
     *
     * @param body body of the response
     * @return true if the response was not already rendered and the body was successfully set
     */
    public boolean setBody(@NonNull String body) {

        if (isRendered()) {

            return false;

        } else {

            this.body = body;

            return true;

        }

    }

}
