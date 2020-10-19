package rocks.gioac96.veronica.session;

import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;

public interface SessionStorage<D> {

    D getSessionData(Request request);

    void setSessionData(Request request, Response.ResponseBuilder responseBuilder, D sessionData);

    boolean clearSessionData(Request request);

    boolean clearAllSessions();

}
