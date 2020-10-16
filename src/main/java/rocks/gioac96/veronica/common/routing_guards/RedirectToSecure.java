package rocks.gioac96.veronica.common.routing_guards;

import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.core.RoutingGuard;
import rocks.gioac96.veronica.providers.ConfigurableProvider;

public class RedirectToSecure extends ConfigurableProvider<RoutingGuard> {

    private boolean isPermanent = false;

    public RedirectToSecure permanent() {

        this.isPermanent = true;
        return this;

    }

    public RedirectToSecure temporary() {

        this.isPermanent = false;
        return this;

    }

    public RedirectToSecure permanent(boolean isPermanent) {

        this.isPermanent = isPermanent;
        return this;

    }

    @Override
    protected RoutingGuard instantiate() {

        if (isPermanent) {

            return request -> {

                if (request.isSecure()) {

                    return null;

                } else {

                    return CommonResponses.permanentRedirect(

                        request.getUri().toString().replaceFirst("^http", "https")

                    );
                }

            };

        } else {

            return request -> {

                if (request.isSecure()) {

                    return null;

                } else {

                    return CommonResponses.temporaryRedirect(

                        request.getUri().toString().replaceFirst("^http", "https")

                    );
                }

            };

        }

    }

}
