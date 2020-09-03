package rocks.gioac96.veronica.common;

import lombok.experimental.UtilityClass;
import rocks.gioac96.veronica.core.HttpMethod;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.RequestMatcher;

/**
 * Contains declaration for common instances of {@link RequestMatcher}.
 */
@UtilityClass
public class CommonRequestMatchers {

    /**
     * Always positive {@link RequestMatcher}.
     *
     * @return an always positive {@link RequestMatcher}
     */
    public RequestMatcher alwaysMatch() {

        return request -> true;

    }

    /**
     * Always negative {@link RequestMatcher}.
     *
     * @return an always negative {@link RequestMatcher}
     */
    public  RequestMatcher neverMatch() {

        return request -> false;

    }

    private  RequestMatcher methodAndPathPattern(
        HttpMethod httpMethod,
        String pathPattern
    ) {

        return request -> request.getHttpMethod() == httpMethod && request.getPath().matches(pathPattern);

    }

    /**
     * Generates a request matcher that matches GET requests that have a pattern matching the specified one.
     *
     * @param pathPattern pattern to check the {@link Request} path against
     * @return the generated request matcher
     */
    @SuppressWarnings("unused")
    public  RequestMatcher get(String pathPattern) {

        return methodAndPathPattern(HttpMethod.GET, pathPattern);

    }

    /**
     * Generates a request matcher that matches favicon requests.
     *
     * @return the generated request matcher
     */
    public RequestMatcher favicon() {

        return request -> request.getHttpMethod() == HttpMethod.GET && request.getPath().equals("/favicon.ico");

    }


    /**
     * Generates a request matcher that matches POST requests that have a pattern matching the specified one.
     *
     * @param pathPattern pattern to check the {@link Request} path against
     * @return the generated request matcher
     */
    @SuppressWarnings("unused")
    public RequestMatcher post(String pathPattern) {

        return methodAndPathPattern(HttpMethod.POST, pathPattern);

    }

    /**
     * Generates a request matcher that matches all requests that have a pattern matching the specified one.
     *
     * @param pathPattern pattern to check the {@link Request} path against
     * @return the generated request matcher
     */
    @SuppressWarnings("unused")
    public RequestMatcher path(String pathPattern) {

        return request -> request.getPath().matches(pathPattern);

    }

}
