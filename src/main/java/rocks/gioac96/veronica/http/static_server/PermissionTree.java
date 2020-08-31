package rocks.gioac96.veronica.http.static_server;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

class PermissionTree {

    protected final Set<PermissionTree> children;
    protected Object permissions;
    protected final Path path;

    int getTreeSize() {

        int counter = 1;

        for (PermissionTree child : children) {

            counter += child.getTreeSize();

        }

        return counter;


    }

    protected PermissionTree(Path path, Object permissions) {

        this.children = new HashSet<>();
        this.path = path;
        this.permissions = permissions;

    }

    public void overwritePermissions(Object permissions) {

        this.permissions = permissions;

        children.clear();

    }

}