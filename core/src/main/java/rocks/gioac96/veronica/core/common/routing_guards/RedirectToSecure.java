package rocks.gioac96.veronica.core.common.routing_guards;

import lombok.NonNull;
import rocks.gioac96.veronica.core.common.CommonResponses;
import rocks.gioac96.veronica.core.RoutingGuard;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.Provider;

public class RedirectToSecure extends ConfigurableProvider<RoutingGuard> {

    protected boolean isPermanent = false;

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

    public RedirectToSecure permanent(@NonNull Provider<Boolean> permanentProvider) {

        return permanent(permanentProvider.provide());

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
