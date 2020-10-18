package rocks.gioac96.veronica.validation.common;

import rocks.gioac96.veronica.validation.common.routes.NoFavicon;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.core.providers.Provider;

/**
 * Framework's common routes.
 */
public final class CommonRoutes {

    private static final Provider<Route> noFavIcon = new NoFavicon();

    /**
     * Gets the framework's common {@link Route} that returns a "not found" response when a "favicon" request is
     * submitted.
     *
     * @return the route
     */
    public static Route noFavIcon() {

        return noFavIcon.provide();

    }

}
