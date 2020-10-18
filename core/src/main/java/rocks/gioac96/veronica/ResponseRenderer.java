package rocks.gioac96.veronica;

import lombok.NonNull;

/**
 * Used to render a {@link Response} and generate the string that is sent as the http response body.
 */
public interface ResponseRenderer {

    String render(@NonNull Response response);

}
