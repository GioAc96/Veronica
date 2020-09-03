package rocks.gioac96.veronica.static_server;

import rocks.gioac96.veronica.core.Request;

/**
 * Interface for object that decides access to a file, given a Request and file permissions.
 * @param <P> Type of the file permissions.
 */
public interface FilePermissionsDecider<P> {

    boolean decide(Request request, P filePermissions);

}
