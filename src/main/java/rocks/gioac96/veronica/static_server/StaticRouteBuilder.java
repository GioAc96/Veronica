package rocks.gioac96.veronica.static_server;

import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.NonNull;
import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.core.Pipeline;
import rocks.gioac96.veronica.core.RequestMatcher;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.providers.ConfigurableProvider;

/**
 * Builder for static server routes.
 *
 * @param <P> the type of the file permissions
 */
public class StaticRouteBuilder<P> extends ConfigurableProvider<Route> {

    private FilePermissionsManager<P> permissionsManager;
    private FilePermissionsDecider<P> permissionDecider;
    private Path baseDir;
    private RequestMatcher requestMatcher = null;
    private Response accessDeniedResponse = CommonResponses.forbidden();
    private Response fileNotFoundResponse = CommonResponses.notFound();
    private ContentDisposition contentDisposition = null;
    private Pipeline.PipelineBuilder pipelineSchematics = null;
    private String basePath = null;

    public StaticRouteBuilder<P> basePath(@NonNull String basePath) {

        if (basePath.endsWith("/")) {

            this.basePath = basePath;

            return requestMatcher(RequestMatcher.builder()
                .pathPattern(basePath + "*")
                .provide());

        } else {

            this.basePath = basePath + "/";

            return requestMatcher(RequestMatcher.builder()
                .pathPattern(basePath + "/*")
                .provide());

        }

    }

    private StaticRouteBuilder<P> requestMatcher(@NonNull RequestMatcher requestMatcher) {

        this.requestMatcher = requestMatcher;
        return this;

    }

    public StaticRouteBuilder<P> customRequestMatcher(
        @NonNull String basePath,
        @NonNull RequestMatcher requestMatcher
    ) {

        this.basePath = basePath;
        return requestMatcher(requestMatcher);

    }


    public StaticRouteBuilder<P> baseDir(@NonNull String baseDir) {

        return baseDir(Paths.get(baseDir));

    }


    public StaticRouteBuilder<P> baseDir(@NonNull Path baseDir) {

        this.baseDir = baseDir.normalize();
        return this;

    }

    public StaticRouteBuilder<P> permissionDecider(@NonNull FilePermissionsDecider<P> permissionDecider) {

        this.permissionDecider = permissionDecider;
        return this;

    }

    public StaticRouteBuilder<P> permissionManager(@NonNull FilePermissionsManager<P> permissionsManager) {

        this.permissionsManager = permissionsManager;
        return this;

    }

    public StaticRouteBuilder<P> pipelineSchematics(@NonNull Pipeline.PipelineBuilder pipelineSchematics) {

        this.pipelineSchematics = pipelineSchematics;
        return this;

    }

    public StaticRouteBuilder<P> disposeInline() {

        this.contentDisposition = ContentDisposition.INLINE;
        return this;

    }

    public StaticRouteBuilder<P> disposeAsAttachment() {

        this.contentDisposition = ContentDisposition.ATTACHMENT;
        return this;

    }

    public StaticRouteBuilder<P> accessDeniedResponse(Response accessDeniedResponse) {

        this.accessDeniedResponse = accessDeniedResponse;
        return this;

    }

    public StaticRouteBuilder<P> fileNotFoundResponse(Response fileNotFoundResponse) {

        this.fileNotFoundResponse = fileNotFoundResponse;
        return this;

    }

    @Override
    protected boolean isValid() {

        return baseDir != null
            && basePath != null
            && permissionDecider != null
            && permissionsManager != null
            && accessDeniedResponse != null
            && fileNotFoundResponse != null;

    }

    @Override
    public Route instantiate() {

        return Route.builder()
            .requestMatcher(requestMatcher)
            .requestHandler(new StaticFileRequestHandler<P>(
                contentDisposition,
                permissionsManager,
                permissionDecider,
                baseDir,
                basePath,
                accessDeniedResponse,
                fileNotFoundResponse,
                pipelineSchematics
            ))
            .provide();

    }

}
