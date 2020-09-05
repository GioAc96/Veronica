package rocks.gioac96.veronica.common;

import rocks.gioac96.veronica.common.routes.NotFound;
import rocks.gioac96.veronica.common.routes.RedirectToSecure;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.providers.Provider;

/**
 * Helper class for common routes.
 */
public final class CommonRoutes {

    private static final Provider<Route> redirectToSecure = new RedirectToSecure();
    private static final Provider<Route> notFound = new NotFound();

    public static Route redirectToSecure() {

        return redirectToSecure.provide();

    }

    public static Route notFound() {

        return notFound.provide();

    }

}
