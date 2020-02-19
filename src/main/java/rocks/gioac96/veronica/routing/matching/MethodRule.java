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
import org.gioac96.veronica.http.HttpMethod;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.util.ArraySet;

/**
 * Request matching rule based on http method of request.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class MethodRule<Q extends Request> implements RequestMatcher<Q> {

    @Getter
    @Setter
    @NonNull
    private ArraySet<HttpMethod> allowedHttpMethods;

    @Generated
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static <Q extends Request> MethodRuleBuilder<Q> builder() {

        return new MethodRuleBuilder<Q>();

    }

    @Override
    public boolean matches(Q request) {

        return allowedHttpMethods.any(
            allowedHttpMethod -> allowedHttpMethod.equals(request.getHttpMethod())
        );

    }

    @Generated
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public static class MethodRuleBuilder<Q extends Request> {

        private @NonNull ArraySet<HttpMethod> allowedHttpMethods = new ArraySet<>();

        public MethodRuleBuilder<Q> allowedMethods(HttpMethod... allowedMethods) {

            Collections.addAll(allowedHttpMethods, allowedMethods);

            return this;

        }

        public MethodRuleBuilder<Q> allowedMethods(Collection<HttpMethod> allowedMethods) {

            this.allowedHttpMethods.addAll(allowedMethods);

            return this;

        }

        public MethodRule<Q> build() {

            return new MethodRule<Q>(allowedHttpMethods);

        }

        public String toString() {

            return
                "MethodRule.MethodRuleBuilder(allowedHttpMethods="
                    + this.allowedHttpMethods + ")";

        }

    }

}
