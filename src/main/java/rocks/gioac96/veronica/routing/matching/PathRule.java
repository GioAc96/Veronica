package rocks.gioac96.veronica.routing.matching;

import java.util.Collection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.util.ArraySet;

/**
 * Request matching rule based on path of request.
 */
@SuppressWarnings("unused")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class PathRule<Q extends Request> implements RequestMatcher<Q> {

    @Getter
    @Setter
    @NonNull
    private ArraySet<String> allowedPathPatterns;

    @Generated
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "unused"})
    public static <Q extends Request> PathRuleBuilder<Q> builder() {

        return new PathRuleBuilder<>();

    }

    @SuppressWarnings("unused")
    @Override
    public boolean matches(Q request) {

        return allowedPathPatterns.any(
            allowedPathPattern -> allowedPathPattern.matches(request.getPath())
        );

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType", "unused"})
    public static class PathRuleBuilder<Q extends Request> {

        private @NonNull
        final ArraySet<String> allowedPathPatterns = new ArraySet<>();

        @SuppressWarnings("unused")
        public PathRuleBuilder<Q> allowedPathPattern(String allowedPathPattern) {

            this.allowedPathPatterns.add(allowedPathPattern);

            return this;

        }

        @SuppressWarnings("unused")
        public PathRuleBuilder<Q> allowedPathPatterns(Collection<String> allowedPathPatterns) {

            this.allowedPathPatterns.addAll(allowedPathPatterns);

            return this;

        }

        @SuppressWarnings("unused")
        public PathRule<Q> build() {

            return new PathRule<>(allowedPathPatterns);

        }

        public String toString() {

            return
                "PathRule.PathRuleBuilder(allowedPathPatterns=" + this.allowedPathPatterns
                    + ")";

        }

    }

}
