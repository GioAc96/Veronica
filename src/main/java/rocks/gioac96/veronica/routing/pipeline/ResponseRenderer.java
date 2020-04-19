package rocks.gioac96.veronica.routing.pipeline;

import lombok.NonNull;
import rocks.gioac96.veronica.http.Response;

/**
 * Used to render a {@link Response} and generate the string that is sent as the http response body.
 */
public interface ResponseRenderer<S extends Response> {

    @SuppressWarnings("RedundantThrows")
    String render(@NonNull S response) throws ResponseRenderingException;

}
