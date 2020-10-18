package rocks.gioac96.veronica.core.static_server;

import java.nio.file.Path;
import rocks.gioac96.veronica.validation.common.CommonResponses;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.core.Response;

class StaticFileRequestHandler<P> implements RequestHandler {

    private final ContentDisposition contentDisposition;
    private final FilePermissionsManager<P> permissionsManager;
    private final FilePermissionsDecider<P> permissionsDecider;
    private final Path baseDir;
    private final String basePath;
    private final Response accessDeniedResponse;
    private final Response fileNotFoundResponse;

    private final RequestHandler requestHandler;

    public StaticFileRequestHandler(
        ContentDisposition contentDisposition,
        FilePermissionsManager<P> permissionsManager,
        FilePermissionsDecider<P> permissionsDecider,
        Path baseDir,
        String basePath,
        Response accessDeniedResponse,
        Response fileNotFoundResponse
    ) {

        this.contentDisposition = contentDisposition;
        this.permissionsManager = permissionsManager;
        this.permissionsDecider = permissionsDecider;
        this.baseDir = baseDir;
        this.basePath = basePath;
        this.accessDeniedResponse = accessDeniedResponse;
        this.fileNotFoundResponse = fileNotFoundResponse;

        this.requestHandler = getDefaultRequestHandler(this);

    }

    private static <P> RequestHandler getDefaultRequestHandler(
        StaticFileRequestHandler<P> instance
    ) {

        return request -> {

            Path requestedFile = instance.resolveFileRequest(request);

            if (instance.canAccess(request, requestedFile)) {

                return instance.disposeFile(requestedFile);

            } else {

                return instance.accessDeniedResponse;

            }

        };

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

    private boolean canAccess(Request request, Path file) {

        P filePermissions = permissionsManager.getPermissions(file);

        return filePermissions != null && permissionsDecider.decide(request, filePermissions);

    }

    private Path resolveFileRequest(Request request) {

        String relativeRequestedPath = request.getPath().substring(basePath.length());
        return baseDir.resolve(relativeRequestedPath);

    }

    @Override
    public Response handle(Request request) {

        return requestHandler.handle(request);

    }

}
