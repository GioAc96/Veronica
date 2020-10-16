package rocks.gioac96.veronica.static_server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

class FilePermissionsManagerTest {

    private static void assertTree(
        FilePermissionsManager<String> filePermissionsManager,
        Consumer<Set<PermissionTree<String>>> assertionsOnTree
    ) {

        try {

            Field rootTreesField = FilePermissionsManager.class.getDeclaredField("rootTrees");
            rootTreesField.setAccessible(true);

            @SuppressWarnings("unchecked")
            Set<PermissionTree<String>> permissionTrees = (Set<PermissionTree<String>>) rootTreesField.get(filePermissionsManager);

            assertionsOnTree.accept(permissionTrees);

        } catch (Exception e) {

            fail();

        }

    }

    @Test
    public void testSetGetRoot() {

        Path root = Paths.get("A:\\");

        FilePermissionsManager.FilePermissionsManagerBuilder<String> builder
            = FilePermissionsManager.builder();

        builder.permissions(root, "A");

        assertEquals(
            "A",
            builder.provide().getPermissions(root)
        );

    }

    @Test
    public void testSetOverwriteGetRoot() {

        Path root = Paths.get("A:\\");

        FilePermissionsManager.FilePermissionsManagerBuilder<String> builder
            = FilePermissionsManager.builder();

        builder.permissions(root, "A");
        builder.permissions(root, "B");

        FilePermissionsManager<String> filePermissionsManager = builder.provide();

        assertEquals(
            "B",
            filePermissionsManager.getPermissions(root)
        );

    }

    @Test
    public void testGetUnknownRootPermissions() {

        Path root = Paths.get("A:\\");

        assertNull(FilePermissionsManager.builder().provide().getPermissions(root));

    }

    @Test
    public void testMultipleRoots() {

        Path rootA = Paths.get("A:\\");
        Path rootB = Paths.get("B:\\");

        FilePermissionsManager.FilePermissionsManagerBuilder<String> builder
            = FilePermissionsManager.builder();

        builder.permissions(rootA, "A");
        builder.permissions(rootB, "B");

        FilePermissionsManager<String> filePermissionsManager = builder.provide();

        assertEquals(
            "A",
            filePermissionsManager.getPermissions(rootA)
        );
        assertEquals(
            "B",
            filePermissionsManager.getPermissions(rootB)
        );

    }

    @Test
    public void testPath() {

        Path pathA = Paths.get("A:\\path");

        FilePermissionsManager.FilePermissionsManagerBuilder<String> builder
            = FilePermissionsManager.builder();

        builder.permissions(pathA, "A");

        FilePermissionsManager<String> filePermissionsManager = builder.provide();

        assertEquals(
            "A",
            filePermissionsManager.getPermissions(pathA)
        );

    }

    @Test
    public void testPathParent() {

        Path pathA = Paths.get("A:\\parent\\child");

        FilePermissionsManager.FilePermissionsManagerBuilder<String> builder
            = FilePermissionsManager.builder();

        builder.permissions(pathA, "A");

        FilePermissionsManager<String> filePermissionsManager = builder.provide();

        assertNull(
            filePermissionsManager.getPermissions(pathA.getParent())
        );


    }

    @Test
    public void testPathChild() {

        Path pathA = Paths.get("A:\\parent");

        FilePermissionsManager.FilePermissionsManagerBuilder<String> builder
            = FilePermissionsManager.builder();

        builder.permissions(pathA, "A");

        FilePermissionsManager<String> filePermissionsManager = builder.provide();

        assertEquals(
            "A",
            filePermissionsManager.getPermissions(Paths.get("A:\\parent\\child"))
        );

    }

    @Test
    public void testParentOverwriteChild() {

        Path parent = Paths.get("A:\\parent");
        Path child = Paths.get("A:\\parent\\child");

        FilePermissionsManager.FilePermissionsManagerBuilder<String> builder
            = FilePermissionsManager.builder();

        builder.permissions(child, "A");
        builder.permissions(parent, "B");

        FilePermissionsManager<String> filePermissionsManager = builder.provide();

        assertEquals(
            "B",
            filePermissionsManager.getPermissions(parent)
        );
        assertEquals(
            "B",
            filePermissionsManager.getPermissions(child)
        );

    }

    @Test
    public void testChildNotOverwritesParent() {

        Path parent = Paths.get("A:\\parent");
        Path child = Paths.get("A:\\parent\\child");

        FilePermissionsManager.FilePermissionsManagerBuilder<String> builder
            = FilePermissionsManager.builder();

        builder.permissions(parent, "B");
        builder.permissions(child, "A");

        FilePermissionsManager<String> filePermissionsManager = builder.provide();

        assertEquals(
            "B",
            filePermissionsManager.getPermissions(parent)
        );
        assertEquals(
            "A",
            filePermissionsManager.getPermissions(child)
        );

    }

    @Test
    public void testTreeCompression() {

        Path parent = Paths.get("A:\\parent");
        Path child = Paths.get("A:\\parent\\child");

        FilePermissionsManager.FilePermissionsManagerBuilder<String> builder
            = FilePermissionsManager.builder();

        builder.permissions(child, "A");
        builder.permissions(parent, "A");

        FilePermissionsManager<String> filePermissionsManager = builder.provide();

        assertEquals(
            "A",
            filePermissionsManager.getPermissions(parent)
        );
        assertEquals(
            "A",
            filePermissionsManager.getPermissions(child)
        );

        assertTree(
            filePermissionsManager,
            rootTrees -> assertEquals(
                2,
                rootTrees.stream().findFirst().get().getTreeSize()
            )
        );

    }

    @Test
    public void testComplexTree1() {

        Path a = Paths.get("A:\\parent\\a");
        Path b = Paths.get("A:\\parent\\a\\b");
        Path c = Paths.get("A:\\parent\\a\\b\\c");
        Path d = Paths.get("A:\\parent\\a\\d");

        FilePermissionsManager.FilePermissionsManagerBuilder<String> builder
            = FilePermissionsManager.builder();

        builder.permissions(a, "A");
        builder.permissions(b, "B");
        builder.permissions(c, "C");
        builder.permissions(d, "D");

        FilePermissionsManager<String> filePermissionsManager = builder.provide();

        assertEquals(
            "A",
            filePermissionsManager.getPermissions(a)
        );
        assertEquals(
            "B",
            filePermissionsManager.getPermissions(b)
        );
        assertEquals(
            "C",
            filePermissionsManager.getPermissions(c)
        );
        assertEquals(
            "D",
            filePermissionsManager.getPermissions(d)
        );

        assertTree(
            filePermissionsManager,
            rootTrees -> assertEquals(
                5,
                rootTrees.stream().findFirst().get().getTreeSize()
            )
        );

        builder.permissions(b, "B");

        filePermissionsManager = builder.provide();

        assertEquals(
            "A",
            filePermissionsManager.getPermissions(a)
        );
        assertEquals(
            "B",
            filePermissionsManager.getPermissions(b)
        );
        assertEquals(
            "B",
            filePermissionsManager.getPermissions(c)
        );
        assertEquals(
            "D",
            filePermissionsManager.getPermissions(d)
        );

        assertTree(
            filePermissionsManager,
            rootTrees -> assertEquals(
                4,
                rootTrees.stream().findFirst().get().getTreeSize()
            )
        );

        builder.permissions(Paths.get("B:\\other_drive"), "O");

        filePermissionsManager = builder.provide();

        assertTree(
            filePermissionsManager,
            rootTrees -> assertEquals(
                2,
                rootTrees.size()
            )
        );

    }

    @Test
    public void testPathNormalization() {

        Path pathA = Paths.get("A:\\parent\\.");
        Path pathB = Paths.get("B:\\parent\\child\\..");

        FilePermissionsManager.FilePermissionsManagerBuilder<String> builder
            = FilePermissionsManager.builder();

        builder.permissions(pathA, "A");
        builder.permissions(pathB, "B");

        FilePermissionsManager<String> filePermissionsManager = builder.provide();

        assertEquals(
            "B",
            filePermissionsManager.getPermissions(pathB)
        );

        assertTree(
            filePermissionsManager,
            rootTrees -> assertEquals(
                2,
                rootTrees.stream().findFirst().get().getTreeSize()
            )
        );

    }

}
