package rocks.gioac96.veronica.common;

import java.nio.file.Path;
import java.nio.file.Paths;
import rocks.gioac96.veronica.common.responses.FileDownload;
import rocks.gioac96.veronica.common.responses.FileInline;
import rocks.gioac96.veronica.common.responses.FileRaw;
import rocks.gioac96.veronica.common.responses.Forbidden;
import rocks.gioac96.veronica.common.responses.InternalError;
import rocks.gioac96.veronica.common.responses.NotFound;
import rocks.gioac96.veronica.common.responses.Ok;
import rocks.gioac96.veronica.common.responses.PromptBasicAuth;
import rocks.gioac96.veronica.common.responses.PromptBasicAuthRealm;
import rocks.gioac96.veronica.common.responses.Redirect;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.providers.Provider;

/**
 * Framework's common responses.
 */
public class CommonResponses {

    private static final Provider<Response> ok = new Ok();
    private static final Provider<Response> notFound = new NotFound();
    private static final Provider<Response> forbidden = new Forbidden();
    private static final Provider<Response> internalError = new InternalError();
    private static final Provider<Response> promptBasicAuth = new PromptBasicAuth();

    /**
     * Gets the framework's common "ok" {@link Response}.
     *
     * @return the response
     */
    public static Response ok() {

        return ok.provide();

    }

    /**
     * Gets the framework's common "not found" {@link Response}.
     *
     * @return the response
     */
    public static Response notFound() {

        return notFound.provide();

    }

    /**
     * Gets the framework's common "forbidden" {@link Response}.
     *
     * @return the response
     */
    public static Response forbidden() {

        return forbidden.provide();

    }

    /**
     * Gets the framework's common "internal error" {@link Response}.
     *
     * @return the response
     */
    public static Response internalError() {

        return internalError.provide();

    }

    /**
     * Gets the framework's common {@link Response} that prompts http basic auth.
     *
     * @return the response
     */
    public static Response promptBasicAuth() {

        return promptBasicAuth.provide();

    }

    /**
     * Gets the framework's common {@link Response} that prompts http basic auth, with the specified realm.
     *
     * @param realm the realm
     * @return the response
     */
    public static Response promptBasicAuth(String realm) {

        if (realm == null) {

            return promptBasicAuth();

        } else {

            return new PromptBasicAuthRealm()
                .realm(realm)
                .provide();

        }

    }

    /**
     * Gets the framework's common {@link Response} containing a raw file.
     *
     * @param filePath the path of the file
     * @return the response
     */
    public static Response fileRaw(String filePath) {

        return fileRaw(Paths.get(filePath));

    }

    /**
     * Gets the framework's common {@link Response} containing a raw file.
     *
     * @param filePath the path of the file
     * @return the response
     */
    public static Response fileRaw(Path filePath) {

        return new FileRaw()
            .filePath(filePath)
            .provide();


    }

    /**
     * Gets the framework's common {@link Response} containing a file attached as a download.
     *
     * @param filePath the path of the file
     * @return the response
     */
    public static Response fileDownload(String filePath) {

        return fileDownload(Paths.get(filePath));

    }

    /**
     * Gets the framework's common {@link Response} containing a file attached as a download.
     *
     * @param filePath the path of the file
     * @return the response
     */
    public static Response fileDownload(Path filePath) {

        return new FileDownload()
            .filePath(filePath)
            .provide();

    }

    /**
     * Gets the framework's common {@link Response} containing a file disposed inline.
     *
     * @param filePath the path of the file
     * @return the response
     */
    public static Response fileInline(String filePath) {

        return fileInline(Paths.get(filePath));

    }

    /**
     * Gets the framework's common {@link Response} containing a file disposed inline.
     *
     * @param filePath the path of the file
     * @return the response
     */
    public static Response fileInline(Path filePath) {

        return new FileInline()
            .filePath(filePath)
            .provide();

    }

    /**
     * Gets the framework's common {@link Response} that permanently redirects to the specified location.
     *
     * @param location the location to redirect to
     * @return the response
     */
    public static Response permanentRedirect(String location) {

        return new Redirect()
            .permanent()
            .location(location)
            .provide();

    }

    /**
     * Gets the framework's common {@link Response} that temporarily redirects to the specified location.
     *
     * @param location the location to redirect to
     * @return the response
     */
    public static Response temporaryRedirect(String location) {

        return new Redirect()
            .temporary()
            .location(location)
            .provide();

    }

}
