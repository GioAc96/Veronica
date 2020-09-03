package rocks.gioac96.veronica.static_server;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

class PermissionTree<P> {

    protected final Set<PermissionTree<P>> children;
    protected P permissions;
    protected final Path path;

    int getTreeSize() {

        int counter = 1;

        for (PermissionTree<P> child : children) {

            counter += child.getTreeSize();

        }

        return counter;


    }

    protected PermissionTree(Path path, P permissions) {

        this.children = new HashSet<>();
        this.path = path;
        this.permissions = permissions;

    }

    public void overwritePermissions(P permissions) {

        this.permissions = permissions;

        children.clear();

    }

}