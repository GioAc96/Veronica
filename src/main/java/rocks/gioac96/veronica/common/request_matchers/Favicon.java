package rocks.gioac96.veronica.common.request_matchers;

import rocks.gioac96.veronica.core.HttpMethod;
import rocks.gioac96.veronica.core.RequestMatcher;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;

/**
 * Framework's common favicon request matcher.
 */
public class Favicon extends RequestMatcher.RequestMatcherBuilder implements BuildsSingleInstance {

    @Override
    protected void configure() {

        super.configure();

        httpMethod(HttpMethod.GET);
        pathPattern("/favicon.ico");

    }

}
