package org.gioac96.veronica.http;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gioac96.veronica.routing.pipeline.ResponseRenderer;

/**
 * Http response.
 */
public class Response {

    @Getter
    private boolean isRendered;

    @Getter
    private String body;

    @Getter
    @Setter
    @NonNull
    protected HttpStatus httpStatus;

    public Response(@NonNull HttpStatus httpStatus) {

        this.httpStatus = httpStatus;

        this.isRendered = false;
        this.body = null;

    }

    /**
     * Renders the response with the specified renderer.
     * @param responseRenderer renderer used to render the response
     */
    public void render(ResponseRenderer responseRenderer) {

        if (isRendered) {

            throw new SecurityException("Response body was already set");

        }

        this.body = responseRenderer.render(this);
        isRendered = true;

    }

}
