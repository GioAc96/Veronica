package rocks.gioac96.veronica.http.static_server;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FilePermissionsManager {

    public static final Object DEFAULT_PERMISSIONS = null;

    private final Set<PermissionTree> rootTrees = new HashSet<>();

    private PermissionTree getClosestParent(Path path) {

        assert !isRoot(path);

        for (PermissionTree rootTree : rootTrees) {

            if (path.getRoot().equals(rootTree.path)) {

                // Root is already known. Descending tree

                PermissionTree pointer = rootTree;

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

        // Root is unknown

        PermissionTree rootTree = new PermissionTree(path.getRoot(), DEFAULT_PERMISSIONS);
        rootTrees.add(rootTree);

        return rootTree;

    }

    private boolean isRoot(Path path) {

        return path.equals(path.getRoot());

    }


    public void setPermissions(Path path, Object permissions) {

        if (isRoot(path)) {

            setRootPermissions(path, permissions);

        } else {

            setNonRootPermissions(path, permissions);

        }


    }

    private void setNonRootPermissions(Path path, Object permissions) {

        assert !isRoot(path);

        PermissionTree closestParent = getClosestParent(path);

        closestParent.children.removeIf(child -> child.path.startsWith(path));

       if (! Objects.equals(permissions, closestParent.permissions)) {

            PermissionTree leaf = new PermissionTree(path, DEFAULT_PERMISSIONS);
            leaf.overwritePermissions(permissions);

            closestParent.children.add(leaf);

        }

    }

    private void setRootPermissions(Path root, Object permissions) {

        assert isRoot(root);

        for (PermissionTree rootTree : rootTrees) {

            if (rootTree.path.equals(root)) {

                // Root is already known

                rootTree.overwritePermissions(permissions);
                return;

            }

        }

        // Root is unknown

        PermissionTree rootTree = new PermissionTree(root, DEFAULT_PERMISSIONS);
        rootTrees.add(rootTree);

        rootTree.overwritePermissions(permissions);

    }

    public Object getPermissions(Path path) {

       if (isRoot(path)) {

           return getRootPermissions(path);

       } else {

           return getNonRootPermissions(path);

       }

    }

    private Object getNonRootPermissions(Path path) {

        assert !isRoot(path);

        PermissionTree closestParent = getClosestParent(path);

        for (PermissionTree child : closestParent.children) {

            if (child.path.equals(path)) {

                return child.permissions;

            }

        }

        return closestParent.permissions;

    }

    private Object getRootPermissions(Path rootPath) {

        assert isRoot(rootPath);

        for (PermissionTree rootTree : rootTrees) {

            if (rootTree.path.equals(rootPath)) {

                return rootTree.permissions;

            }

        }

        return DEFAULT_PERMISSIONS;

    }

}
