package rocks.gioac96.veronica.common;

import rocks.gioac96.veronica.common.request_handlers.NotFound;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.providers.Provider;

public class CommonRequestHandlers {

    private static final Provider<RequestHandler> notFound = new NotFound();

    public static RequestHandler notFound() {

        return notFound.provide();

    }

}
