package rocks.gioac96.veronica.routing.matching;

import java.util.Arrays;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.http.HttpMethod;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.util.ArraySet;

/**
 * Request matching rule based on http method of request.
 */
@SuppressWarnings("unused")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class MethodRule implements RequestMatcher {

    @Getter
    @Setter
    @NonNull
    private ArraySet<HttpMethod> allowedHttpMethods;

    @Generated
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "unused"})
    public static MethodRuleBuilder builder() {

        return new MethodRuleBuilder();

    }

    @SuppressWarnings("unused")
    @Override
    public boolean matches(Request request) {

        return allowedHttpMethods.any(
            allowedHttpMethod -> allowedHttpMethod.equals(request.getHttpMethod())
        );

    }

    @Generated
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType", "unused"})
    public static class MethodRuleBuilder {

        @NonNull
        private final ArraySet<HttpMethod> allowedHttpMethods = new ArraySet<>();

        @SuppressWarnings("unused")
        public MethodRuleBuilder allowedMethod(HttpMethod allowedMethod) {

            this.allowedHttpMethods.add(allowedMethod);

            return this;

        }

        @SuppressWarnings("unused")
        public MethodRuleBuilder allowedMethods(Collection<HttpMethod> allowedMethods) {

            this.allowedHttpMethods.addAll(allowedMethods);

            return this;

        }

        public MethodRuleBuilder allowedMethods(HttpMethod... allowedMethods) {

            return allowedMethods(Arrays.asList(allowedMethods));

        }

        @SuppressWarnings("unused")
        public MethodRule build() {

            return new MethodRule(allowedHttpMethods);

        }

    }

}
