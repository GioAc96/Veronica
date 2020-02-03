package org.gioac96.veronica.routing;

import org.gioac96.veronica.http.Request;

/**
 * Checks that a {@link Request} object matches a specified condition.
 */
public interface RequestMatcher {

    boolean matches(Request request);

}
