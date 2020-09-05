package rocks.gioac96.veronica.common.request_matchers;

import lombok.NonNull;
import rocks.gioac96.veronica.core.HttpMethod;
import rocks.gioac96.veronica.core.RequestMatcher;

public class MethodAndPathPattern extends PathPattern {

    protected HttpMethod httpMethod;

    public MethodAndPathPattern httpMethod(@NonNull HttpMethod httpMethod) {

        this.httpMethod = httpMethod;

        return this;

    }

    @Override
    protected boolean isValid() {

        return super.isValid() && isNotNull(
            httpMethod
        );

    }

    @Override
    protected RequestMatcher instantiate() {

        return super.instantiate().and(request -> request.getHttpMethod().equals(httpMethod));

    }

}
