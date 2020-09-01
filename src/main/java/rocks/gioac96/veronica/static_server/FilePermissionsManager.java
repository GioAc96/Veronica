package rocks.gioac96.veronica.static_server;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FilePermissionsManager<P> {

    private final Set<PermissionTree<P>> rootTrees = new HashSet<>();

    private PermissionTree<P> getClosestParent(Path path) {

        assert !isRoot(path);

        for (PermissionTree<P> rootTree : rootTrees) {

            if (path.getRoot().equals(rootTree.path)) {

                // Root is already known. Descending tree

                PermissionTree<P> pointer = rootTree;

                loop:
                while (pointer.path.getNameCount() < path.getNameCount()) {

                    for (PermissionTree<P> node : pointer.children) {

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

        // Root is unknown

        PermissionTree<P> rootTree = new PermissionTree<>(path.getRoot(), null);
        rootTrees.add(rootTree);

        return rootTree;

    }

    private boolean isRoot(Path path) {

        return path.equals(path.getRoot());

    }

    public void setPermissions(Path path, P permissions) {

        Path normalizedPath = path.normalize();

        if (isRoot(normalizedPath)) {

            setRootPermissions(normalizedPath, permissions);

        } else {

            setNonRootPermissions(normalizedPath, permissions);

        }


    }

    private void setNonRootPermissions(Path path, P permissions) {

        assert !isRoot(path);

        PermissionTree<P> closestParent = getClosestParent(path);

        closestParent.children.removeIf(child -> child.path.startsWith(path));

       if (! Objects.equals(permissions, closestParent.permissions)) {

            PermissionTree<P> leaf = new PermissionTree<>(path, null);
            leaf.overwritePermissions(permissions);

            closestParent.children.add(leaf);

        }

    }

    private void setRootPermissions(Path root, P permissions) {

        assert isRoot(root);

        for (PermissionTree<P> rootTree : rootTrees) {

            if (rootTree.path.equals(root)) {

                // Root is already known

                rootTree.overwritePermissions(permissions);
                return;

            }

        }

        // Root is unknown

        PermissionTree<P> rootTree = new PermissionTree<>(root, null);
        rootTrees.add(rootTree);

        rootTree.overwritePermissions(permissions);

    }

    public P getPermissions(Path path) {

        Path normalizedPath = path.normalize();

        if (isRoot(normalizedPath)) {

            return getRootPermissions(normalizedPath);
        } else {

            return getNonRootPermissions(normalizedPath);

        }

    }

    private P getNonRootPermissions(Path path) {

        assert !isRoot(path);

        PermissionTree<P> closestParent = getClosestParent(path);

        for (PermissionTree<P> child : closestParent.children) {

            if (child.path.equals(path)) {

                return child.permissions;

            }

        }

        return closestParent.permissions;

    }

    private P getRootPermissions(Path rootPath) {

        assert isRoot(rootPath);

        for (PermissionTree<P> rootTree : rootTrees) {

            if (rootTree.path.equals(rootPath)) {

                return rootTree.permissions;

            }

        }

        return null;

    }

}
