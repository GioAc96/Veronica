package rocks.gioac96.veronica.statics;

import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.NonNull;
import rocks.gioac96.veronica.http.CommonResponses;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.pipeline.Pipeline;

/**
 * Builder for static server routes.
 * @param <P> the type of the file permissions
 */
@SuppressWarnings("checkstyle:MissingJavadocMethod")
public class StaticRouteBuilder<
        P
    > {

    @NonNull
    private FilePermissionsManager<P> permissionsManager = new FilePermissionsManager<>();

    @NonNull
    private FilePermissionsDecider<P> permissionDecider = ((request, filePermissions) -> false);

    @NonNull
    private Path baseDir;

    @NonNull
    private String basePath;

    @NonNull
    private Response accessDeniedResponse = CommonResponses.forbidden();

    @NonNull
    private Response fileNotFoundResponse = CommonResponses.notFound();

    @NonNull
    private Pipeline pipeline = Pipeline.builder().build();

    private ContentDisposition contentDisposition = null;

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

            return CommonResponses.inlineFile(file);

        } else if (contentDisposition == ContentDisposition.ATTACHMENT) {

            return CommonResponses.attachmentFile(file);

        } else {

            return CommonResponses.rawFile(file);

        }

    }

    public Route build() {

        if (
            baseDir == null || basePath == null
        ) {

            throw new NullPointerException("Both baseDir and basePath must be set");

        }

        return Route.builder()
            .pipeline(pipeline)
            .requestMatcher(request -> request.getPath().startsWith(basePath))
            .handler(request -> {

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
