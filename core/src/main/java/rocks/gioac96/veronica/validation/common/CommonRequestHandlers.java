package rocks.gioac96.veronica.validation.common;

import rocks.gioac96.veronica.validation.common.request_handlers.NotFound;
import rocks.gioac96.veronica.validation.common.request_handlers.Ok;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.core.providers.Provider;

/**
 * Framework's common request handlers.
 */
public class CommonRequestHandlers {

    private static final Provider<RequestHandler> notFound = new NotFound();
    private static final Provider<RequestHandler> ok = new Ok();

    /**
     * Gets the framework's common "not found" {@link RequestHandler}.
     *
     * @return the request handler
     */
    public static RequestHandler notFound() {

        return notFound.provide();

    }

    /**
     * Gets the framework's common "ok" {@link RequestHandler}.
     *
     * @return the request handler
     */
    public static RequestHandler ok() {

        return ok.provide();

    }

}
