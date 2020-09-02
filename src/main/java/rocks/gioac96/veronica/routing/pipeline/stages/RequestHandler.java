package rocks.gioac96.veronica.routing.pipeline.stages;

import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.pipeline.Pipeline;

/**
 * Stage of the {@link Pipeline} responsible for generating a {@link Response}.
 */
public interface RequestHandler {

    Response handle(Request request);

}
