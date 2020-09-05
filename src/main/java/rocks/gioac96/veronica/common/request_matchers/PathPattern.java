package rocks.gioac96.veronica.common.request_matchers;

import lombok.NonNull;
import rocks.gioac96.veronica.core.RequestMatcher;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;

public class PathPattern extends Builder<RequestMatcher> implements BuildsMultipleInstances {

    private String pathPattern;

    public PathPattern pathPattern(@NonNull String pathPattern) {

        this.pathPattern = pathPattern;

        return this;

    }

    @Override
    protected boolean isValid() {

        return isNotNull(pathPattern);

    }

    @Override
    protected RequestMatcher instantiate() {

        return request -> request.getPath().matches(pathPattern);

    }

}
