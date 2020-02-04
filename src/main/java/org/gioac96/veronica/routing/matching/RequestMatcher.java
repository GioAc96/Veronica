package org.gioac96.veronica.routing.matching;

import org.gioac96.veronica.http.Request;

public abstract class RequestMatcher {

    public abstract boolean matches(Request request);

    public static RequestMatcher alwaysMatch() {

        return new RequestMatcher() {

            @Override
            public boolean matches(Request request) {

                return true;

            }

        };

    }

}
