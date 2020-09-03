package rocks.gioac96.veronica.statics;

import rocks.gioac96.veronica.http.Request;

/**
 * Interface for object that decides access to a file, given a Request and file permissions.
 * @param <P> Type of the file permissions.
 */
public interface FilePermissionsDecider<P> {

    boolean decide(Request request, P filePermissions);

}
