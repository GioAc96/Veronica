package rocks.gioac96.veronica.static_server;

import rocks.gioac96.veronica.http.Request;

public interface FilePermissionsDecider<Q extends Request, P> {

    boolean decide(Q request, P filePermissions);

}
