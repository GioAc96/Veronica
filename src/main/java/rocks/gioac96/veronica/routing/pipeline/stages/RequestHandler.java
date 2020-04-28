package rocks.gioac96.veronica.routing.pipeline.stages;

import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.pipeline.Pipeline;

/**
 * Stage of the {@link Pipeline} responsible for generating a {@link Response}.
 */
public interface RequestHandler<Q extends Request, S extends Response> {

    RequestHandlerPayload<S> handle(Q request);

}
