package org.gioac96.veronica.routing.matching;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.util.ArraySet;

/**
 * Checks that a {@link Request} object matches a specified condition.
 */
@NoArgsConstructor
public class RulesRequestMatcher extends RequestMatcher {

    @Getter
    @Setter
    @NonNull
    private ArraySet<RequestMatchingRule> matchingRules = new ArraySet<>();

    public RulesRequestMatcher(@NonNull RequestMatchingRule rule) {

        matchingRules = ArraySet.of(RequestMatchingRule.class, rule);

    }

    /**
     * Checks whether a request matches the matching rules.
     * @param request request to check
     * @return true iff the request matches the matching rules
     */
    public boolean matches(Request request) {

        return matchingRules.every(matchingRule -> matchingRule.matches(request));

    }



}
