package rocks.gioac96.veronica.routing.matching;

import rocks.gioac96.veronica.http.Request;

/**
 * Request matching rule.
 */
public interface RequestMatcher<Q extends Request> {

    boolean matches(Q request);

}
