package rocks.gioac96.veronica.common;

import rocks.gioac96.veronica.common.request_handlers.NotFound;
import rocks.gioac96.veronica.common.request_handlers.Ok;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.providers.Provider;

public class CommonRequestHandlers {

    private static final Provider<RequestHandler> notFound = new NotFound();
    private static final Provider<RequestHandler> ok = new Ok();

    public static RequestHandler notFound() {

        return notFound.provide();

    }

    public static RequestHandler ok() {

        return ok.provide();

    }

}
