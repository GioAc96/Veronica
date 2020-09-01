package rocks.gioac96.veronica.static_server;

import rocks.gioac96.veronica.http.Request;

/**
 * Interface for object that decides access to a file, given a Request and file permissions.
 * @param <Q> Type of the request
 * @param <P> Type of the file permissions.
 */
public interface FilePermissionsDecider<Q extends Request, P> {

    boolean decide(Q request, P filePermissions);

}
