package rocks.gioac96.veronica.common.request_matchers;

import rocks.gioac96.veronica.core.RequestMatcher;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;

public class NeverMatch extends Builder<RequestMatcher> implements BuildsSingleInstance {

    @Override
    protected RequestMatcher instantiate() {

        return request -> false;

    }


}
