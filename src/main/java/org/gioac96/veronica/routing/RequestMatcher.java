package org.gioac96.veronica.routing;

import org.gioac96.veronica.http.Request;

public interface RequestMatcher {

    boolean matches(Request request);

}
