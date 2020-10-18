package rocks.gioac96.veronica.routing.common;

import rocks.gioac96.veronica.core.HttpMethod;
import rocks.gioac96.veronica.core.providers.Provider;
import rocks.gioac96.veronica.routing.RequestMatcher;
import rocks.gioac96.veronica.routing.common.request_matchers.Favicon;

/**
 * Framework's common request matchers.
 */
public class CommonRequestMatchers {

    private static final Provider<RequestMatcher> favicon = new Favicon();

    /**
     * Gets the framework's common "favicon" {@link RequestMatcher}.
     *
     * @return the request matcher
     */
    public static RequestMatcher favicon() {

        return favicon.provide();

    }

    /**
     * Gets the framework's common {@link RequestMatcher} for "GET" requests that match the specified path pattern.
     *
     * @param pathPattern the pattern the request's path matches against
     * @return the request matcher
     */
    public static RequestMatcher get(String pathPattern) {

        return RequestMatcher.builder()
            .httpMethod(HttpMethod.GET)
            .pathPattern(pathPattern)
            .provide();

    }

    /**
     * Gets the framework's common {@link RequestMatcher} for "POST" requests that match the specified path pattern.
     *
     * @param pathPattern the pattern the request's path matches against
     * @return the request matcher
     */
    public static RequestMatcher post(String pathPattern) {

        return RequestMatcher.builder()
            .httpMethod(HttpMethod.POST)
            .pathPattern(pathPattern)
            .provide();

    }

    /**
     * Gets the framework's common {@link RequestMatcher} for "PUT" requests that match the specified path pattern.
     *
     * @param pathPattern the pattern the request's path matches against
     * @return the request matcher
     */
    public static RequestMatcher put(String pathPattern) {

        return RequestMatcher.builder()
            .httpMethod(HttpMethod.PUT)
            .pathPattern(pathPattern)
            .provide();

    }

    /**
     * Gets the framework's common {@link RequestMatcher} for "HEAD" requests that match the specified path pattern.
     *
     * @param pathPattern the pattern the request's path matches against
     * @return the request matcher
     */
    public static RequestMatcher head(String pathPattern) {

        return RequestMatcher.builder()
            .httpMethod(HttpMethod.HEAD)
            .pathPattern(pathPattern)
            .provide();

    }

    /**
     * Gets the framework's common {@link RequestMatcher} for "DELETE" requests that match the specified path pattern.
     *
     * @param pathPattern the pattern the request's path matches against
     * @return the request matcher
     */
    public static RequestMatcher delete(String pathPattern) {

        return RequestMatcher.builder()
            .httpMethod(HttpMethod.DELETE)
            .pathPattern(pathPattern)
            .provide();

    }

}
