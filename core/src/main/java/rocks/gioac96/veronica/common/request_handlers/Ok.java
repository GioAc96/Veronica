package rocks.gioac96.veronica.common.request_handlers;

import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.providers.ConfigurableProvider;
import rocks.gioac96.veronica.providers.Singleton;

public class Ok
    extends ConfigurableProvider<RequestHandler>
    implements Singleton {

    @Override
    protected RequestHandler instantiate() {

        return request -> CommonResponses.ok();

    }

}
