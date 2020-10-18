package rocks.gioac96.veronica.core;

import rocks.gioac96.veronica.core.pipeline.Pipeline;

/**
 * Stage of the {@link Pipeline} responsible for generating a {@link Response}.
 */
public interface RequestHandler {

    Response handle(Request request);

}
