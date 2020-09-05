package rocks.gioac96.veronica.common.request_matchers;

import rocks.gioac96.veronica.core.RequestMatcher;
import rocks.gioac96.veronica.providers.Builder;

public class AlwaysMatch extends Builder<RequestMatcher> {

    @Override
    protected RequestMatcher instantiate() {

        return request -> true;

    }

}
