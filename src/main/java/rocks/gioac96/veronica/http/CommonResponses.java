package rocks.gioac96.veronica.http;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import rocks.gioac96.veronica.http.static_server.MimeResolver;

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

    private MimeResolver getMimeResolver() {

        if (mimeResolver == null) {

            mimeResolver = MimeResolver.basic().build();

        }

        return mimeResolver;

    }

    /**
     * Instantiates a response with a blank body.
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

    public Response embeddedFile(String filePath) {

        Path path = Paths.get(filePath);

        try {

            return file(path)
                .header("Content-Disposition", "inline; filename=" + path.getFileName())
                .build();

        } catch (IOException e) {

            return notFound();

        }

    }

    public Response downloadFile(String filePath) {

        Path path = Paths.get(filePath);

        try {

            return file(path)
                .header("Content-Disposition", "attachment; filename=" + path.getFileName())
                .build();

        } catch (IOException e) {

            return notFound();

        }

    }

}
