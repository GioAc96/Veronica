package rocks.gioac96.veronica.common.request_handlers;

import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;

public class Ok extends Builder<RequestHandler> implements BuildsSingleInstance {

    @Override
    protected RequestHandler instantiate() {

        return request -> CommonResponses.ok();

    }

}
