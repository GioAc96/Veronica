package rocks.gioac96.veronica.core;

import lombok.NonNull;

/**
 * Request matching rule.
 */
public interface RequestMatcher {

    boolean matches(Request request);

    default RequestMatcher and(@NonNull RequestMatcher other) {

        return request -> this.matches(request) && other.matches(request);

    }

    default RequestMatcher or(@NonNull RequestMatcher other) {

        return request -> this.matches(request) || other.matches(request);

    }

    default RequestMatcher negate() {

        return request -> ! this.matches(request);

    }

}
