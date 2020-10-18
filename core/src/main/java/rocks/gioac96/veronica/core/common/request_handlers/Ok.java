package rocks.gioac96.veronica.core.common.request_handlers;

import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.core.common.CommonResponses;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.Singleton;

public class Ok
    extends ConfigurableProvider<RequestHandler>
    implements Singleton {

    @Override
    protected RequestHandler instantiate() {

        return request -> CommonResponses.ok();

    }

}
