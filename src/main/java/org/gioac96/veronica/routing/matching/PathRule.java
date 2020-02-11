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
 * Request matching rule based on path of request.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class PathRule implements RequestMatchingRule {

    @Getter
    @Setter
    @NonNull
    private ArraySet<String> allowedPathPatterns;

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static PathRuleBuilder builder() {

        return new PathRuleBuilder();

    }

    @Override
    public boolean matches(Request request) {

        return allowedPathPatterns.any(
            allowedPathPattern -> allowedPathPattern.matches(request.getPath())
        );

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public static class PathRuleBuilder {

        private ArraySet<String> allowedPathPatterns = new ArraySet<>();

        public PathRuleBuilder allowedPathPattern(String... allowedPathPatterns) {

            Collections.addAll(this.allowedPathPatterns, allowedPathPatterns);

            return this;

        }

        public PathRuleBuilder allowedPathPatterns(Collection<String> allowedPathPatterns) {

            this.allowedPathPatterns.addAll(allowedPathPatterns);

            return this;

        }

        public PathRule build() {

            return new PathRule(
                allowedPathPatterns
            );

        }

    }

}
