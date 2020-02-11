package org.gioac96.veronica.routing.matching;

import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.util.ArraySet;

/**
 * Checks that a {@link Request} object matches a specified condition.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class RulesRequestMatcher implements RequestMatcher {

    @Getter
    @Setter
    @NonNull
    private ArraySet<RequestMatchingRule> matchingRules;

    public RulesRequestMatcher(@NonNull RequestMatchingRule rule) {

        matchingRules = ArraySet.of(RequestMatchingRule.class, rule);

    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static RulesRequestMatcherBuilder builder() {

        return new RulesRequestMatcherBuilder();

    }

    /**
     * Checks whether a request matches all the matching rules.
     *
     * @param request request to check
     * @return true iff the request matches the matching rules
     */
    public boolean matches(Request request) {

        return matchingRules.every(matchingRule -> matchingRule.matches(request));

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public static class RulesRequestMatcherBuilder {

        private ArraySet<RequestMatchingRule> requestMatchingRules = new ArraySet<>();

        public RulesRequestMatcherBuilder requestMatchingRules(RequestMatchingRule... requestMatchingRules) {

            Collections.addAll(this.requestMatchingRules, requestMatchingRules);

            return this;

        }

        public RulesRequestMatcherBuilder requestMatchingRules(Collection<RequestMatchingRule> requestMatchingRules) {

            this.requestMatchingRules.addAll(requestMatchingRules);

            return this;

        }

        public RulesRequestMatcher build() {

            return new RulesRequestMatcher(
                requestMatchingRules
            );

        }

    }

}
