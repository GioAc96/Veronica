package rocks.gioac96.veronica.common;

import rocks.gioac96.veronica.common.request_matchers.AlwaysMatch;
import rocks.gioac96.veronica.common.request_matchers.Favicon;
import rocks.gioac96.veronica.common.request_matchers.MethodAndPathPattern;
import rocks.gioac96.veronica.common.request_matchers.NeverMatch;
import rocks.gioac96.veronica.core.HttpMethod;
import rocks.gioac96.veronica.core.RequestMatcher;
import rocks.gioac96.veronica.providers.Provider;

/**
 * Contains declaration for common instances of {@link RequestMatcher}.
 */
public class CommonRequestMatchers {

    private static final Provider<RequestMatcher> neverMatch = new NeverMatch();
    private static final Provider<RequestMatcher> alwaysMatch = new AlwaysMatch();
    private static final Provider<RequestMatcher> favicon = new Favicon();

    public static RequestMatcher neverMatch() {

        return neverMatch.provide();

    }

    public static RequestMatcher alwaysMatch() {

        return alwaysMatch.provide();

    }

    public static RequestMatcher favicon() {

        return favicon.provide();

    }

    public static RequestMatcher methodAndPathPattern(HttpMethod httpMethod, String pathPattern) {

        return new MethodAndPathPattern()
            .httpMethod(httpMethod)
            .pathPattern(pathPattern)
            .build();

    }

    public static RequestMatcher get(String pathPattern) {

        return methodAndPathPattern(HttpMethod.GET, pathPattern);

    }

    public static RequestMatcher post(String pathPattern) {

        return methodAndPathPattern(HttpMethod.POST, pathPattern);

    }

    public static RequestMatcher put(String pathPattern) {

        return methodAndPathPattern(HttpMethod.PUT, pathPattern);

    }

    public static RequestMatcher head(String pathPattern) {

        return methodAndPathPattern(HttpMethod.HEAD, pathPattern);

    }

    public static RequestMatcher delete(String pathPattern) {

        return methodAndPathPattern(HttpMethod.DELETE, pathPattern);

    }

}
