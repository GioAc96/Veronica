package rocks.gioac96.veronica.routing.common;

import rocks.gioac96.veronica.core.providers.Provider;
import rocks.gioac96.veronica.routing.RoutingGuard;
import rocks.gioac96.veronica.routing.common.routing_guards.RedirectToSecure;

/**
 * Framework's common routing guards.
 */
public class CommonRoutingGuards {

    private static final Provider<RoutingGuard> redirectToSecure = new RedirectToSecure();

    /**
     * Gets the framework's common {@link RoutingGuard} that redirects all non-secure requests to a secure context.
     *
     * @return the response
     */
    public static RoutingGuard redirectToSecure() {

        return redirectToSecure.provide();

    }

}
