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
import rocks.gioac96.veronica.validation.ValidationFailureData;
import rocks.gioac96.veronica.validation.ValidationFailureResponse;

public class CommonResponses {

    private static final Provider<Response> ok = new Ok();
    private static final Provider<Response> notFound = new NotFound();
    private static final Provider<Response> forbidden = new Forbidden();
    private static final Provider<Response> internalError = new InternalError();
    private static final Provider<Response> promptBasicAuth = new PromptBasicAuth();

    public static Response ok() {

        return ok.provide();

    }

    public static Response notFound() {

        return notFound.provide();

    }

    public static Response forbidden() {

        return forbidden.provide();

    }

    public static Response internalError() {

        return internalError.provide();

    }

    public static Response promptBasicAuth() {

        return promptBasicAuth.provide();

    }

    public static Response promptBasicAuth(String realm) {

        if (realm == null) {

            return promptBasicAuth();

        } else {

            return new PromptBasicAuthRealm()
                .realm(realm)
                .provide();

        }

    }

    public static Response fileRaw(String filePath) {

        return fileRaw(Paths.get(filePath));

    }


    public static Response fileRaw(Path filePath) {

        return new FileRaw()
            .filePath(filePath)
            .provide();


    }

    public static Response fileDownload(String filePath) {

        return fileDownload(Paths.get(filePath));

    }

    public static Response fileDownload(Path filePath) {

        return new FileDownload()
            .filePath(filePath)
            .provide();

    }

    public static Response fileInline(String filePath) {

        return fileInline(Paths.get(filePath));

    }

    public static Response fileInline(Path filePath) {

        return new FileInline()
            .filePath(filePath)
            .provide();

    }

    public static Response permanentRedirect(String location) {

        return new Redirect()
            .permanent()
            .location(location)
            .provide();

    }

    public static Response temporaryRedirect(String location) {

        return new Redirect()
            .temporary()
            .location(location)
            .provide();

    }

    public static Response validationFailure(ValidationFailureData validationFailureData) {

        return ValidationFailureResponse.builder()
            .validationFailureData(validationFailureData)
            .build();

    }

}
