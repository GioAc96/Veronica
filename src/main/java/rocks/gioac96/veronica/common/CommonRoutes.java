package rocks.gioac96.veronica.common;

import rocks.gioac96.veronica.common.routes.NoFavicon;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.providers.Provider;

/**
 * Helper class for common routes.
 */
public final class CommonRoutes {

    private static final Provider<Route> noFavIcon = new NoFavicon();

    public static Route noFavIcon() {

        return noFavIcon.provide();

    }

}
