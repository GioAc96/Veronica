package rocks.gioac96.veronica.http.static_server;

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

            Set<PermissionTree<String>> permissionTrees = (Set<PermissionTree<String>>) rootTreesField.get(filePermissionsManager);

            assertionsOnTree.accept(permissionTrees);

        } catch (Exception e) {

            fail();

        }

    }

    @Test
    public void testSetGetRoot() {

        Path root = Paths.get("A:\\");

        FilePermissionsManager<String> filePermissionsManager = new FilePermissionsManager<>();

        filePermissionsManager.setPermissions(root, "A");

        assertEquals(
            "A",
            filePermissionsManager.getPermissions(root)
        );

    }

    @Test
    public void testSetOverwriteGetRoot() {

        Path root = Paths.get("A:\\");

        FilePermissionsManager<String> filePermissionsManager = new FilePermissionsManager<>();

        filePermissionsManager.setPermissions(root, "A");
        filePermissionsManager.setPermissions(root, "B");

        assertEquals(
            "B",
            filePermissionsManager.getPermissions(root)
        );

    }
    @Test
    public void testGetUnknownRootPermissions() {

        Path root = Paths.get("A:\\");

        FilePermissionsManager<String> filePermissionsManager = new FilePermissionsManager<>();

        assertNull(filePermissionsManager.getPermissions(root));

    }

    @Test
    public void testMultipleRoots() {

        Path rootA = Paths.get("A:\\");
        Path rootB = Paths.get("B:\\");

        FilePermissionsManager<String> filePermissionsManager = new FilePermissionsManager<>();

        filePermissionsManager.setPermissions(rootA, "A");
        filePermissionsManager.setPermissions(rootB, "B");

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

        FilePermissionsManager<String> filePermissionsManager = new FilePermissionsManager<>();

        filePermissionsManager.setPermissions(pathA, "A");

        assertEquals(
            "A",
            filePermissionsManager.getPermissions(pathA)
        );

    }

    @Test
    public void testPathParent() {

        Path pathA = Paths.get("A:\\parent\\child");

        FilePermissionsManager<String> filePermissionsManager = new FilePermissionsManager<>();

        filePermissionsManager.setPermissions(pathA, "A");

        assertNull(
            filePermissionsManager.getPermissions(pathA.getParent())
        );


    }

    @Test
    public void testPathChild() {

        Path pathA = Paths.get("A:\\parent");

        FilePermissionsManager<String> filePermissionsManager = new FilePermissionsManager<>();

        filePermissionsManager.setPermissions(pathA, "A");

        assertEquals(
            "A",
            filePermissionsManager.getPermissions(Paths.get("A:\\parent\\child"))
        );

    }

    @Test
    public void testParentOverwriteChild() {

        Path parent = Paths.get("A:\\parent");
        Path child = Paths.get("A:\\parent\\child");

        FilePermissionsManager<String> filePermissionsManager = new FilePermissionsManager<>();

        filePermissionsManager.setPermissions(child, "A");
        filePermissionsManager.setPermissions(parent, "B");

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

        FilePermissionsManager<String> filePermissionsManager = new FilePermissionsManager<>();

        filePermissionsManager.setPermissions(parent, "B");
        filePermissionsManager.setPermissions(child, "A");

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

        FilePermissionsManager<String> filePermissionsManager = new FilePermissionsManager<>();

        filePermissionsManager.setPermissions(child, "A");
        filePermissionsManager.setPermissions(parent, "A");

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

        FilePermissionsManager<String> filePermissionsManager = new FilePermissionsManager<>();

        filePermissionsManager.setPermissions(a, "A");
        filePermissionsManager.setPermissions(b, "B");
        filePermissionsManager.setPermissions(c, "C");
        filePermissionsManager.setPermissions(d, "D");

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

        filePermissionsManager.setPermissions(b, "B");

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

        filePermissionsManager.setPermissions(Paths.get("B:\\other_drive"), "O");

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

        FilePermissionsManager<String> filePermissionsManager = new FilePermissionsManager<>();

        filePermissionsManager.setPermissions(pathA, "A");
        filePermissionsManager.setPermissions(pathB, "B");

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
