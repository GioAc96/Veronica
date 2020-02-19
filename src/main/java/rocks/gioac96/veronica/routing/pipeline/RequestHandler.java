package rocks.gioac96.veronica.routing.pipeline;

import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;

/**
 * Stage of the {@link Pipeline} responsible for generating a {@link Response}.
 */
public interface RequestHandler<Q extends Request, S extends Response> {

    S handle(Q request) throws PipelineBreakException;

}
