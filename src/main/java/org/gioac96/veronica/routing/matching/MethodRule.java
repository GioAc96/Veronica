package org.gioac96.veronica.routing.matching;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gioac96.veronica.http.HttpMethod;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.util.ArraySet;

/**
 * Request matching rule based on http method of request.
 */
@AllArgsConstructor
public class MethodRule implements RequestMatchingRule {

    @Getter
    @Setter
    @NonNull
    private ArraySet<HttpMethod> allowedHttpMethods;


    @Override
    public boolean matches(Request request) {

        return allowedHttpMethods.any(
            allowedHttpMethod -> allowedHttpMethod.equals(request.getHttpMethod())
        );

    }

}
