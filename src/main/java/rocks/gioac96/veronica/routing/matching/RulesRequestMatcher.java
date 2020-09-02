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
public final class RulesRequestMatcher implements RequestMatcher {

    @Getter
    @Setter
    @NonNull
    private ArraySet<RequestMatcher> matchingRules;

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "unused"})
    public static  RulesRequestMatcherBuilder builder() {

        return new RulesRequestMatcherBuilder();

    }

    /**
     * Checks whether a request matches all the matching rules.
     *
     * @param request request to check
     * @return true iff the request matches the matching rules
     */
    @SuppressWarnings("unused")
    public boolean matches(Request request) {

        return matchingRules.every(matchingRule -> matchingRule.matches(request));

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType", "unused"})
    public static class RulesRequestMatcherBuilder {

        private final ArraySet<RequestMatcher> requestMatchingRules = new ArraySet<>();

        @SuppressWarnings("unused")
        public RulesRequestMatcherBuilder requestMatchingRule(RequestMatcher requestMatchingRule) {

            this.requestMatchingRules.add(requestMatchingRule);

            return this;

        }

        @SuppressWarnings("unused")
        public RulesRequestMatcherBuilder requestMatchingRules(Collection<RequestMatcher> requestMatchingRules) {

            this.requestMatchingRules.addAll(requestMatchingRules);

            return this;

        }

        @SuppressWarnings("unused")
        public RulesRequestMatcher build() {

            return new RulesRequestMatcher(
                requestMatchingRules
            );

        }

    }

}
