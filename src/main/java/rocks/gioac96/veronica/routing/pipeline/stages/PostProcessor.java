package rocks.gioac96.veronica.routing.pipeline.stages;

import lombok.NonNull;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.pipeline.Pipeline;

/**
 * {@link Pipeline} stage that processes a {@link Request} after the {@link Response} was already sent.
 */
public interface PostProcessor<Q extends Request, S extends Response> {

    void process(@NonNull Q request, @NonNull S response);

    interface Asynchronous<Q extends Request, S extends Response> extends PostProcessor<Q, S> {}

}
