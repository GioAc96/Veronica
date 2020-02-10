package org.gioac96.veronica.routing.matching;

import org.gioac96.veronica.http.Request;

/**
 * Checks whether a specified {@link Request} matches a certain criteria.
 */
public interface RequestMatcher {

    /**
     * Checks whether a specified {@link Request} matches a certain criteria.
     * @param request {@link Request} to check
     * @return true iff the specified {@link Request} matches the defined criteria
     */
    boolean matches(Request request);

}
