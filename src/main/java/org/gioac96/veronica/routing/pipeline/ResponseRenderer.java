package org.gioac96.veronica.routing.pipeline;

import lombok.NonNull;
import org.gioac96.veronica.http.Response;

public interface ResponseRenderer {

    String render(@NonNull Response response);

}
