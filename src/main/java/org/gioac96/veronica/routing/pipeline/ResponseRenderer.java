package org.gioac96.veronica.routing.pipeline;

import lombok.NonNull;
import org.gioac96.veronica.http.Response;

/**
 * Used to render a {@link Response} and generate the string that is sent as the http response body.
 */
public interface ResponseRenderer<S extends Response> {

    String render(@NonNull S response) throws ResponseRenderingException;

}
