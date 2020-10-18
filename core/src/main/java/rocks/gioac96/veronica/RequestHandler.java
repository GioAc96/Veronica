package rocks.gioac96.veronica;

import rocks.gioac96.veronica.pipeline.Pipeline;

/**
 * Stage of the {@link Pipeline} responsible for generating a {@link Response}.
 */
public interface RequestHandler {

    Response handle(Request request);

}
