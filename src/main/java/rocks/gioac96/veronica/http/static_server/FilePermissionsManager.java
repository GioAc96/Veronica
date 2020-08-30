package rocks.gioac96.veronica.http.static_server;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class FilePermissionsManager {

    private class PermissionTree {

        private Object permissions = null;
        private final Set<PermissionTree> children = new HashSet<>();
        private final Path path;

        private PermissionTree(Path path) {

            this.path = path;

        }

        public void setPermissions(Object permissions) {

            this.permissions = permissions;

            children.clear();

        }

    }

    private final Set<PermissionTree> roots = new HashSet<>();

    private PermissionTree getClosestParent(Path path) {

        for (PermissionTree root : roots) {

            if (path.getRoot().equals(root.path)) {

                PermissionTree pointer = root;

                loop:
                while (pointer.path.getNameCount() < path.getNameCount()) {

                    for (PermissionTree node : pointer.children) {

                        if (path.equals(node.path)) {

                            return pointer;

                        } else if (path.startsWith(node.path)) {

                            pointer = node;
                            continue loop;

                        }

                    }

                    break;

                }

                return pointer;

            }

        }

        PermissionTree root = new PermissionTree(path.getRoot());
        roots.add(root);

        if (path.getRoot().equals(root.path)) {

            return root;

        } else {

            PermissionTree leaf = new PermissionTree(path);
            root.children.add(leaf);

            return leaf;

        }
    }

    public void setPermissions(Path path, Object permissions) {

        PermissionTree closestParent = getClosestParent(path);

        closestParent.children.removeIf(child -> child.path.startsWith(path));

        if (! closestParent.permissions.equals(permissions)) {

            PermissionTree leaf = new PermissionTree(path);
            leaf.setPermissions(permissions);

            closestParent.children.add(leaf);

        }

    }

    public Object getPermissions(Path path) {

        PermissionTree closestParent = getClosestParent(path);

        for (PermissionTree child : closestParent.children) {

            if (child.path.equals(path)) {

                return child.permissions;

            }

        }

        return closestParent.permissions;

    }

}
