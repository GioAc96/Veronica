package rocks.gioac96.veronica.core;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.Provider;

/**
 * Request matching conditions.
 */
@Getter
public class RequestMatcher {

    private final Set<String> pathPatterns;

    private final Set<HttpMethod> httpMethods;

    private final Set<Predicate<Request>> conditions;

    private RequestMatcher(RequestMatcherBuilder b) {

        this.pathPatterns = b.pathPatterns;
        this.httpMethods = b.httpMethods;
        this.conditions = b.conditions;

    }

    public static RequestMatcherBuilder builder() {

        class RequestMatcherBuilderImpl
            extends RequestMatcherBuilder
            implements BuildsMultipleInstances {
        }

        return new RequestMatcherBuilderImpl();

    }

    public static abstract class RequestMatcherBuilder extends Builder<RequestMatcher> {

        private final Set<String> pathPatterns = new HashSet<>();
        private final Set<HttpMethod> httpMethods = new HashSet<>();
        private final Set<Predicate<Request>> conditions = new HashSet<>();

        public RequestMatcherBuilder pathPattern(@NonNull String pathPattern) {

            this.pathPatterns.add(pathPattern);

            return this;

        }

        public RequestMatcherBuilder pathPattern(@NonNull Provider<String> pathPattern) {

            return pathPattern(pathPattern.provide());

        }

        public RequestMatcherBuilder httpMethod(@NonNull HttpMethod httpMethod) {

            this.httpMethods.add(httpMethod);

            return this;

        }

        public RequestMatcherBuilder httpMethod(@NonNull Provider<HttpMethod> httpMethod) {

            return httpMethod(httpMethod);

        }

        public RequestMatcherBuilder condition(@NonNull Predicate<Request> condition) {

            this.conditions.add(condition);

            return this;

        }

        public RequestMatcherBuilder condition(@NonNull Provider<Predicate<Request>> condition) {

            return condition(condition.provide());

        }

        @Override
        protected boolean isValid() {

            return super.isValid()
                && pathPatterns.size() > 0;

        }

        @Override
        protected RequestMatcher instantiate() {

            return new RequestMatcher(this);

        }

    }

}
