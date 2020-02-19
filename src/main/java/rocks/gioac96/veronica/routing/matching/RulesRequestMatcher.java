package rocks.gioac96.veronica.routing.matching;

import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.util.ArraySet;

/**
 * Checks that a {@link Request} object matches a set of specified rules.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class RulesRequestMatcher<Q extends Request> implements RequestMatcher<Q> {

    @Getter
    @Setter
    @NonNull
    private ArraySet<RequestMatcher<Q>> matchingRules;

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static <Q extends Request> RulesRequestMatcherBuilder<Q> builder() {

        return new RulesRequestMatcherBuilder<Q>();

    }

    /**
     * Checks whether a request matches all the matching rules.
     *
     * @param request request to check
     * @return true iff the request matches the matching rules
     */
    public boolean matches(Q request) {

        return matchingRules.every(matchingRule -> matchingRule.matches(request));

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public static class RulesRequestMatcherBuilder<Q extends Request> {

        private ArraySet<RequestMatcher<Q>> requestMatchingRules = new ArraySet<>();

        public RulesRequestMatcherBuilder<Q> requestMatchingRules(RequestMatcher<Q>... requestMatchingRules) {

            Collections.addAll(this.requestMatchingRules, requestMatchingRules);

            return this;

        }

        public RulesRequestMatcherBuilder<Q> requestMatchingRules(Collection<RequestMatcher<Q>> requestMatchingRules) {

            this.requestMatchingRules.addAll(requestMatchingRules);

            return this;

        }

        public RulesRequestMatcher<Q> build() {

            return new RulesRequestMatcher<Q>(
                requestMatchingRules
            );

        }

    }

}
