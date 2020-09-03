package rocks.gioac96.veronica.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.static_server.MimeResolver;

/**
 * Utility class for instantiating common Response objects.
 */
@UtilityClass
public class CommonResponses {

    @Setter
    private MimeResolver mimeResolver = null;

    @Setter
    private Response ok = null;

    @Setter
    private Response notFound = null;

    @Setter
    private Response internalError = null;

    @Setter
    private Response forbidden = null;

    private MimeResolver getMimeResolver() {

        if (mimeResolver == null) {

            mimeResolver = MimeResolver.basic().build();

        }

        return mimeResolver;

    }

    /**
     * Instantiates a response with a blank body.
     *
     * @param httpStatus the http status of the response
     * @return the instantiated response
     */
    public Response empty(HttpStatus httpStatus) {

        return Response.builder()
            .httpStatus(httpStatus)
            .emptyBody()
            .build();

    }

    /**
     * Instantiates an http "OK" response with an empty body.
     * @return the instantiated response
     */
    public Response ok() {

        if (ok == null) {

            ok = empty(HttpStatus.OK);

        }

        return ok;

    }

    /**
     * Instantiates an http "OK" response.
     * @param body body of the response
     * @return the instantiated response
     */
    public Response ok(String body) {

        return Response.builder()
            .httpStatus(HttpStatus.OK)
            .body(body)
            .build();

    }



    /**
     * Instantiates an http "NOT FOUND" response with an empty body.
     * @return the instantiated response
     */
    public Response notFound() {

        if (notFound == null) {

            notFound = empty(HttpStatus.NOT_FOUND);

        }

        return notFound;

    }


    /**
     * Instantiates an http "INTERNAL SERVER ERROR" response with an empty body.
     * @return the instantiated response
     */
    public Response internalError() {

        if (internalError == null) {

            internalError = empty(HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return internalError;

    }

    private Response.ResponseBuilder<?, ?> file(
        Path filePath
    ) throws IOException {

        String mime = getMimeResolver().resolveMime(filePath.getFileName().toString());

        Response.ResponseBuilder<?, ?> builder = Response.builder();

        if (mime != null) {

            builder.header("Content-Type", mime);

        }

        builder.body(Files.readAllBytes(filePath));

        return builder;

    }

    /**
     * Instantiates an http "FORBIDDEN" response with an empty body.
     * @return the instantiated response
     */
    public Response forbidden() {

        if (forbidden == null) {

            forbidden = empty(HttpStatus.UNAUTHORIZED);

        }

        return forbidden;

    }

    /**
     * Instantiates an http containing a file disposed inline.
     *
     * @param filePath the path of the file
     * @return the instantiated response
     */
    public Response inlineFile(String filePath) {

        return inlineFile(Paths.get(filePath));


    }

    /**
     * Instantiates an http containing a file disposed inline.
     *
     * @param filePath the path of the file
     * @return the instantiated response
     */
    public Response inlineFile(Path filePath) {

        try {

            return file(filePath)
                .header("Content-Disposition", "inline; filename=" + filePath.getFileName())
                .build();

        } catch (IOException e) {

            return notFound();

        }

    }

    /**
     * Instantiates an http containing a file disposed as attachment.
     *
     * @param filePath the path of the file
     * @return the instantiated response
     */
    public Response attachmentFile(String filePath) {

        return attachmentFile(Paths.get(filePath));

    }

    /**
     * Instantiates an http containing a file disposed as attachment.
     *
     * @param filePath the path of the file
     * @return the instantiated response
     */
    public Response attachmentFile(Path filePath) {

        try {

            return file(filePath)
                .header("Content-Disposition", "attachment; filename=" + filePath.getFileName())
                .build();

        } catch (IOException e) {

            return notFound();

        }

    }

    /**
     * Instantiates an http containing a raw file.
     *
     * @param filePath the path of the file
     * @return the instantiated response
     */
    public Response rawFile(Path filePath) {

        try {

            return file(filePath).build();

        } catch (IOException e) {

            return notFound();

        }

    }

    /**
     * Instantiates an http containing a raw file.
     *
     * @param filePath the path of the file
     * @return the instantiated response
     */
    public Response rawFile(String filePath) {

        return rawFile(Paths.get(filePath));

    }

    /**
     * Instantiates a response that prompts http basic authentication.
     * @return the instantiated response
     */
    public Response promptBasicAuth() {

        return promptBasicAuth(null);

    }

    /**
     * Instantiates a response that prompts http basic authentication.
     * @param realm the realm
     * @return the instantiated response
     */
    public Response promptBasicAuth(String realm) {

        if (realm == null) {

            return Response.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .header("WWW-Authenticate", "Basic")
                .build();

        } else {

            return Response.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .header("WWW-Authenticate", "Basic realm=\"" + realm + "\"")
                .build();

        }

    }

}
