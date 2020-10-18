package rocks.gioac96.veronica.file_server;

import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.NonNull;
import rocks.gioac96.veronica.core.common.CommonResponses;
import rocks.gioac96.veronica.routing.RequestMatcher;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.Provider;

/**
 * Builder for static server routes.
 *
 * @param <P> the type of the file permissions
 */
public class StaticRouteBuilder<P> extends ConfigurableProvider<Route> {

    protected FilePermissionsManager<P> permissionsManager;
    protected FilePermissionsDecider<P> permissionDecider;
    protected Path baseDir;
    protected RequestMatcher requestMatcher;
    protected Response accessDeniedResponse = CommonResponses.forbidden();
    protected Response fileNotFoundResponse = CommonResponses.notFound();
    protected ContentDisposition contentDisposition;
    protected String basePath;

    public StaticRouteBuilder<P> basePath(@NonNull String basePath) {

        if (basePath.endsWith("/")) {

            this.basePath = basePath;

            this.requestMatcher = RequestMatcher.builder()
                .pathPattern(basePath + "*")
                .provide();

        } else {

            this.basePath = basePath + "/";

            this.requestMatcher = RequestMatcher.builder()
                .pathPattern(basePath + "/*")
                .provide();


        }

        return this;

    }

    public StaticRouteBuilder<P> basePath(@NonNull Provider<String> basePathProvider) {

        return basePath(basePathProvider.provide());

    }

    public StaticRouteBuilder<P> customRequestMatcher(
        @NonNull String basePath,
        @NonNull RequestMatcher requestMatcher
    ) {

        this.basePath = basePath;
        this.requestMatcher = requestMatcher;
        return this;

    }

    public StaticRouteBuilder<P> baseDir(@NonNull Path baseDir) {

        this.baseDir = baseDir.normalize();
        return this;

    }

    public StaticRouteBuilder<P> baseDir(@NonNull String baseDir) {

        return baseDir(Paths.get(baseDir));

    }

    public StaticRouteBuilder<P> baseDir(@NonNull Provider<Path> baseDirProvider) {

        return baseDir(baseDirProvider.provide());

    }

    public StaticRouteBuilder<P> permissionDecider(@NonNull FilePermissionsDecider<P> permissionDecider) {

        this.permissionDecider = permissionDecider;
        return this;

    }

    public StaticRouteBuilder<P> permissionDecider(@NonNull Provider<FilePermissionsDecider<P>> permissionDeciderProvider) {

        return permissionDecider(permissionDeciderProvider.provide());

    }

    public StaticRouteBuilder<P> permissionManager(@NonNull FilePermissionsManager<P> permissionsManager) {

        this.permissionsManager = permissionsManager;
        return this;

    }

    public StaticRouteBuilder<P> permissionManager(@NonNull Provider<FilePermissionsManager<P>> permissionsManagerProvider) {

        return permissionManager(permissionsManagerProvider.provide());

    }
    public StaticRouteBuilder<P> contentDisposition(@NonNull ContentDisposition contentDisposition) {

        this.contentDisposition = contentDisposition;
        return this;

    }

    public StaticRouteBuilder<P> contentDisposition(@NonNull Provider<ContentDisposition> contentDispositionProvider) {

        return contentDisposition(contentDispositionProvider.provide());

    }

    public StaticRouteBuilder<P> disposeInline() {

        return contentDisposition(ContentDisposition.INLINE);

    }

    public StaticRouteBuilder<P> disposeAsAttachment() {

        return contentDisposition(ContentDisposition.ATTACHMENT);

    }

    public StaticRouteBuilder<P> accessDeniedResponse(@NonNull Response accessDeniedResponse) {

        this.accessDeniedResponse = accessDeniedResponse;
        return this;

    }
    
    public StaticRouteBuilder<P> accessDeniedResponse(@NonNull Provider<Response> accessDeniedResponseProvider) {

        return accessDeniedResponse(accessDeniedResponseProvider.provide());

    }

    public StaticRouteBuilder<P> fileNotFoundResponse(@NonNull Response fileNotFoundResponse) {

        this.fileNotFoundResponse = fileNotFoundResponse;
        return this;

    }

    public StaticRouteBuilder<P> fileNotFoundResponse(@NonNull Provider<Response> fileNotFoundResponseProvider) {

        return fileNotFoundResponse(fileNotFoundResponseProvider.provide());

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
                fileNotFoundResponse
            ))
            .provide();

    }

}
