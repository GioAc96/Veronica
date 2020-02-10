package org.gioac96.veronica.routing.matching;

import lombok.experimental.UtilityClass;
import org.gioac96.veronica.http.HttpMethod;
import org.gioac96.veronica.http.Request;

/**
 * Contains declaration for common request matchers.
 */
@UtilityClass
public class CommonRequestMatchers {
    
    /**
     * Always positive {@link RequestMatcher}.
     * @return an always positive {@link RequestMatcher}
     */
    public static RequestMatcher alwaysMatch() {

        return request -> true;

    }

    private static RequestMatcher methodAndPathPattern(HttpMethod httpMethod, String pathPattern) {

        return request -> request.getHttpMethod() == httpMethod && request.getPath().matches(pathPattern);

    }

    /**
     * Generates a request matcher that matches GET requests that have a pattern matching the specified one.
     * @param pathPattern pattern to check the {@link Request} path against
     * @return the generated request matcher
     */
    public static RequestMatcher get(String pathPattern) {

        return methodAndPathPattern(HttpMethod.GET, pathPattern);

    }


    /**
     * Generates a request matcher that matches POST requests that have a pattern matching the specified one.
     * @param pathPattern pattern to check the {@link Request} path against
     * @return the generated request matcher
     */
    public static RequestMatcher post(String pathPattern) {

        return methodAndPathPattern(HttpMethod.POST, pathPattern);

    }

}
