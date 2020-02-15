package org.gioac96.veronica.routing.matching;

import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.util.ArraySet;

/**
 * Request matching rule based on path of request.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class PathRule<Q extends Request> implements RequestMatcher<Q> {

    @Getter
    @Setter
    @NonNull
    private ArraySet<String> allowedPathPatterns;

    @Generated
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static <Q extends Request> PathRuleBuilder<Q> builder() {

        return new PathRuleBuilder<Q>();

    }

    @Override
    public boolean matches(Q request) {

        return allowedPathPatterns.any(
            allowedPathPattern -> allowedPathPattern.matches(request.getPath())
        );

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public static class PathRuleBuilder<Q extends Request> {

        private @NonNull ArraySet<String> allowedPathPatterns = new ArraySet<>();

        public PathRuleBuilder<Q> allowedPathPatterns(String... allowedPathPatterns) {

            Collections.addAll(this.allowedPathPatterns, allowedPathPatterns);

            return this;

        }

        public PathRuleBuilder<Q> allowedPathPatterns(Collection<String> allowedPathPatterns) {

            this.allowedPathPatterns.addAll(allowedPathPatterns);

            return this;

        }

        public PathRule<Q> build() {

            return new PathRule<Q>(allowedPathPatterns);

        }

        public String toString() {

            return
                "PathRule.PathRuleBuilder(allowedPathPatterns=" + this.allowedPathPatterns
                    + ")";

        }

    }

}
