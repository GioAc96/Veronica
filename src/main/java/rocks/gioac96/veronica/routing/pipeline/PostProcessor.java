package rocks.gioac96.veronica.routing.pipeline;

import lombok.NonNull;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;

/**
 * {@link Pipeline} stage that processes a {@link Request} after the {@link Response} was already sent.
 */
public interface PostProcessor<Q extends Request, S extends Response> {

    void process(@NonNull Q request, @NonNull S response);

}
