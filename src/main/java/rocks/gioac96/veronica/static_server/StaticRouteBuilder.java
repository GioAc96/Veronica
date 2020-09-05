package rocks.gioac96.veronica.static_server;

import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.NonNull;
import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.core.Pipeline;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;

/**
 * Builder for static server routes.
 * @param <P> the type of the file permissions
 */
@SuppressWarnings("checkstyle:MissingJavadocMethod")
public class StaticRouteBuilder<
        P
    > extends Builder<Route>
    implements BuildsMultipleInstances {

    private FilePermissionsManager<P> permissionsManager;

    private FilePermissionsDecider<P> permissionDecider;

    private Path baseDir;

    private String basePath;

    private Response accessDeniedResponse = CommonResponses.forbidden();

    private Response fileNotFoundResponse = CommonResponses.notFound();

    private ContentDisposition contentDisposition = null;

    private Pipeline pipeline = Pipeline.builder().build();

    private enum ContentDisposition {

        INLINE,
        ATTACHMENT

    }

    public StaticRouteBuilder<P> basePath(@NonNull String basePath) {

        if (basePath.endsWith("/")) {

            this.basePath = basePath;

        } else {

            this.basePath = basePath + '/';

        }

        return this;

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

    public StaticRouteBuilder<P> pipeline(@NonNull Pipeline pipeline) {

        this.pipeline = pipeline;
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

    private Path resolveFileRequest(Request request) {
        
        String relativeRequestedPath = request.getPath().substring(basePath.length());
        return baseDir.resolve(relativeRequestedPath);
        
    }
    
    private boolean canAccess(Request request, Path file) {
        
        P filePermissions = permissionsManager.getPermissions(file);
        
        return filePermissions != null && permissionDecider.decide(request, filePermissions);
        
    }
    
    private Response disposeFile(Path file) {

        if (contentDisposition == ContentDisposition.INLINE) {

            return CommonResponses.fileInline(file);

        } else if (contentDisposition == ContentDisposition.ATTACHMENT) {

            return CommonResponses.fileDownload(file);

        } else {

            return CommonResponses.fileRaw(file);

        }

    }

    @Override
    protected boolean isValid() {

        return isNotNull(
            baseDir,
            basePath,
            permissionDecider,
            permissionsManager,
            accessDeniedResponse,
            fileNotFoundResponse,
            pipeline
        );

    }

    @Override
    public Route instantiate() {

        return Route.builder()
            .pipeline(pipeline)
            .requestMatcher(request -> request.getPath().startsWith(basePath))
            .requestHandler(request -> {

                Path requestedFile = resolveFileRequest(request);

                if (canAccess(request, requestedFile)) {

                    return disposeFile(requestedFile);

                } else {

                    return accessDeniedResponse;

                }

            })
            .build();

    }

}
