package rocks.gioac96.veronica.common;

import rocks.gioac96.veronica.common.request_matchers.Favicon;
import rocks.gioac96.veronica.core.HttpMethod;
import rocks.gioac96.veronica.core.RequestMatcher;
import rocks.gioac96.veronica.providers.Provider;

/**
 * Contains declaration for common instances of {@link RequestMatcher}.
 */
public class CommonRequestMatchers {

    private static final Provider<RequestMatcher> favicon = new Favicon();

    public static RequestMatcher favicon() {

        return favicon.provide();

    }

    public static RequestMatcher get(String pathPattern) {

        return RequestMatcher.builder()
            .httpMethod(HttpMethod.GET)
            .pathPattern(pathPattern)
            .build();

    }

    public static RequestMatcher post(String pathPattern) {

        return RequestMatcher.builder()
            .httpMethod(HttpMethod.POST)
            .pathPattern(pathPattern)
            .build();

    }

    public static RequestMatcher put(String pathPattern) {

        return RequestMatcher.builder()
            .httpMethod(HttpMethod.PUT)
            .pathPattern(pathPattern)
            .build();

    }

    public static RequestMatcher head(String pathPattern) {

        return RequestMatcher.builder()
            .httpMethod(HttpMethod.HEAD)
            .pathPattern(pathPattern)
            .build();

    }

    public static RequestMatcher delete(String pathPattern) {

        return RequestMatcher.builder()
            .httpMethod(HttpMethod.DELETE)
            .pathPattern(pathPattern)
            .build();

    }

}
