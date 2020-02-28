package rocks.gioac96.veronica.routing.matching;

import java.util.Collection;
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
@SuppressWarnings("unused")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class RulesRequestMatcher<Q extends Request> implements RequestMatcher<Q> {

    @Getter
    @Setter
    @NonNull
    private ArraySet<RequestMatcher<Q>> matchingRules;

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "unused"})
    public static <Q extends Request> RulesRequestMatcherBuilder<Q> builder() {

        return new RulesRequestMatcherBuilder<>();

    }

    /**
     * Checks whether a request matches all the matching rules.
     *
     * @param request request to check
     * @return true iff the request matches the matching rules
     */
    @SuppressWarnings("unused")
    public boolean matches(Q request) {

        return matchingRules.every(matchingRule -> matchingRule.matches(request));

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType", "unused"})
    public static class RulesRequestMatcherBuilder<Q extends Request> {

        private final ArraySet<RequestMatcher<Q>> requestMatchingRules = new ArraySet<>();

        @SuppressWarnings("unused")
        public RulesRequestMatcherBuilder<Q> requestMatchingRule(RequestMatcher<Q> requestMatchingRule) {

            this.requestMatchingRules.add(requestMatchingRule);

            return this;

        }

        @SuppressWarnings("unused")
        public RulesRequestMatcherBuilder<Q> requestMatchingRules(Collection<RequestMatcher<Q>> requestMatchingRules) {

            this.requestMatchingRules.addAll(requestMatchingRules);

            return this;

        }

        @SuppressWarnings("unused")
        public RulesRequestMatcher<Q> build() {

            return new RulesRequestMatcher<>(
                requestMatchingRules
            );

        }

    }

}
