package rocks.gioac96.veronica.common;

import rocks.gioac96.veronica.common.routing_guards.RedirectToSecure;
import rocks.gioac96.veronica.core.RoutingGuard;
import rocks.gioac96.veronica.providers.Provider;

public class CommonRoutingGuards {

    private static final Provider<RoutingGuard> redirectToSecure = new RedirectToSecure();

    public static RoutingGuard redirectToSecure() {

        return redirectToSecure.provide();

    }

}
