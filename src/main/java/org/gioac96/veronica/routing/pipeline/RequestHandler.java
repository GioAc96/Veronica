package org.gioac96.veronica.routing.pipeline;

import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.http.Response;

public interface RequestHandler {

    Response handle(Request request);

}
