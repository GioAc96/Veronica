package org.gioac96.veronica.routing.matching;

import org.gioac96.veronica.http.Request;

/**
 * Request matching rule.
 */
public interface RequestMatchingRule {

    boolean matches(Request request);

}
