package org.gioac96.veronica.routing.matching;

import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gioac96.veronica.http.HttpMethod;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.util.ArraySet;

/**
 * Request matching rule based on http method of request.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class MethodRule implements RequestMatchingRule {

    @Getter
    @Setter
    @NonNull
    private ArraySet<HttpMethod> allowedHttpMethods;

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static MethodRuleBuilder builder() {

        return new MethodRuleBuilder();

    }

    @Override
    public boolean matches(Request request) {

        return allowedHttpMethods.any(
            allowedHttpMethod -> allowedHttpMethod.equals(request.getHttpMethod())
        );

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public static class MethodRuleBuilder {

        private ArraySet<HttpMethod> allowedHttpMethods = new ArraySet<>();

        public MethodRuleBuilder allowedMethods(HttpMethod... allowedMethods) {

            Collections.addAll(allowedHttpMethods, allowedMethods);

            return this;

        }

        public MethodRuleBuilder allowedMethods(Collection<HttpMethod> allowedMethods) {

            this.allowedHttpMethods.addAll(allowedMethods);

            return this;

        }

        public MethodRule build() {

            return new MethodRule(
                allowedHttpMethods
            );

        }

    }

}
