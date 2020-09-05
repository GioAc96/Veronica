package rocks.gioac96.veronica.common.routes;

import rocks.gioac96.veronica.common.CommonRequestMatchers;

public class NoFavicon extends NotFound {

    @Override
    protected void configure() {

        super.configure();

        requestMatcher(CommonRequestMatchers.favicon());

    }

}
