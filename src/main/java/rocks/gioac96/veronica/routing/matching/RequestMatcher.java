package rocks.gioac96.veronica.routing.matching;

import rocks.gioac96.veronica.http.Request;

/**
 * Request matching rule.
 */
public interface RequestMatcher {

    boolean matches(Request request);

}
