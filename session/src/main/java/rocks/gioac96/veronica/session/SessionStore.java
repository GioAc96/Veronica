package rocks.gioac96.veronica.session;

import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;

public interface SessionStore<D> {

    D getSessionData(Request request);

    void setSessionData(Request request, Response.ResponseBuilder responseBuilder, D sessionData);

    void clearSessionData(Request request);

    void clearAllSessions();

}
