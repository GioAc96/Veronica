package rocks.gioac96.veronica.core;

/**
 * Stage of the {@link Pipeline} responsible for generating a {@link Response}.
 */
public interface RequestHandler {

    Response handle(Request request);

}
