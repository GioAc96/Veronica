package rocks.gioac96.veronica.routing.matching;

import lombok.experimental.UtilityClass;
import rocks.gioac96.veronica.http.HttpMethod;
import rocks.gioac96.veronica.http.Request;

/**
 * Contains declaration for common instances of {@link RequestMatcher}.
 */
@UtilityClass
public class CommonRequestMatchers {

    /**
     * Always positive {@link RequestMatcher}.
     *
     * @param <Q> Type of the request to match
     * @return an always positive {@link RequestMatcher}
     */
    public <Q extends Request> RequestMatcher<Q> alwaysMatch() {

        return request -> true;

    }

    /**
     * Always negative {@link RequestMatcher}.
     *
     * @param <Q> Type of the request to match
     * @return an always negative {@link RequestMatcher}
     */
    public <Q extends Request> RequestMatcher<Q> neverMatch() {

        return request -> false;

    }

    private <Q extends Request> RequestMatcher<Q> methodAndPathPattern(
        HttpMethod httpMethod,
        String pathPattern
    ) {

        return request -> request.getHttpMethod() == httpMethod && request.getPath().matches(pathPattern);

    }

    /**
     * Generates a request matcher that matches GET requests that have a pattern matching the specified one.
     *
     * @param <Q>         Type of the request to match
     * @param pathPattern pattern to check the {@link Request} path against
     * @return the generated request matcher
     */
    @SuppressWarnings("unused")
    public <Q extends Request> RequestMatcher<Q> get(String pathPattern) {

        return methodAndPathPattern(HttpMethod.GET, pathPattern);

    }


    /**
     * Generates a request matcher that matches POST requests that have a pattern matching the specified one.
     *
     * @param <Q>         Type of the request to match
     * @param pathPattern pattern to check the {@link Request} path against
     * @return the generated request matcher
     */
    @SuppressWarnings("unused")
    public <Q extends Request> RequestMatcher<Q> post(String pathPattern) {

        return methodAndPathPattern(HttpMethod.POST, pathPattern);

    }

    /**
     * Generates a request matcher that matches all requests that have a pattern matching the specified one.
     *
     * @param <Q>         Type of the request to match
     * @param pathPattern pattern to check the {@link Request} path against
     * @return the generated request matcher
     */
    @SuppressWarnings("unused")
    public <Q extends Request> RequestMatcher<Q> path(String pathPattern) {

        return request -> request.getPath().matches(pathPattern);

    }

}
