package rocks.gioac96.veronica.core.static_server;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

class PermissionTree<P> {

    protected final Set<PermissionTree<P>> children;
    protected final Path path;
    protected P permissions;

    protected PermissionTree(Path path, P permissions) {

        this.children = new HashSet<>();
        this.path = path;
        this.permissions = permissions;

    }

    int getTreeSize() {

        int counter = 1;

        for (PermissionTree<P> child : children) {

            counter += child.getTreeSize();

        }

        return counter;


    }

    public void overwritePermissions(P permissions) {

        this.permissions = permissions;

        children.clear();

    }

}