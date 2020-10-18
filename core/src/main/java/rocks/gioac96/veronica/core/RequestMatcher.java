package rocks.gioac96.veronica.core;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.core.providers.Provider;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;

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

        return new RequestMatcherBuilder();

    }

    public static class RequestMatcherBuilder extends ConfigurableProvider<RequestMatcher> {

        protected Set<String> pathPatterns = new HashSet<>();
        protected Set<HttpMethod> httpMethods = new HashSet<>();
        protected Set<Predicate<Request>> conditions = new HashSet<>();

        public RequestMatcherBuilder pathPattern(@NonNull String pathPattern) {

            this.pathPatterns.add(pathPattern);

            return this;

        }

        public RequestMatcherBuilder pathPattern(@NonNull Provider<String> pathPatternProvider) {

            return pathPattern(pathPatternProvider.provide());

        }

        public RequestMatcherBuilder httpMethod(@NonNull HttpMethod httpMethod) {

            this.httpMethods.add(httpMethod);

            return this;

        }

        public RequestMatcherBuilder httpMethod(@NonNull Provider<HttpMethod> httpMethodProvider) {

            return httpMethod(httpMethodProvider.provide());

        }

        public RequestMatcherBuilder condition(@NonNull Predicate<Request> condition) {

            this.conditions.add(condition);

            return this;

        }

        public RequestMatcherBuilder condition(@NonNull Provider<Predicate<Request>> conditionProvider) {

            return condition(conditionProvider.provide());

        }

        @Override
        protected boolean isValid() {

            return super.isValid()
                && pathPatterns != null
                && !pathPatterns.isEmpty()
                && pathPatterns.stream().noneMatch(Objects::isNull);

        }

        @Override
        protected RequestMatcher instantiate() {

            return new RequestMatcher(this);

        }

    }

}
