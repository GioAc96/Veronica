package rocks.gioac96.veronica.common.request_matchers;

import rocks.gioac96.veronica.HttpMethod;
import rocks.gioac96.veronica.RequestMatcher;
import rocks.gioac96.veronica.providers.Singleton;

/**
 * Framework's common favicon request matcher.
 */
public class Favicon
    extends RequestMatcher.RequestMatcherBuilder
    implements Singleton {

    @Override
    protected void configure() {

        httpMethod(HttpMethod.GET);
        pathPattern("/favicon.ico");

        super.configure();

    }

}
