package rocks.gioac96.veronica.providers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BuilderTest {

    @Test
    void testSingleInstance() {

        class BuilderA extends Builder<String> implements BuildsSingleInstance {

            @Override
            protected String instantiate() {

                return new String("Ciao");

            }

        }

        assertSame(new BuilderA().build(), new BuilderA().build());

    }

    @Test
    void testMultipleInstances() {

        class BuilderA extends Builder<String> implements BuildsMultipleInstances {

            @Override
            protected String instantiate() {

                return new String("Ciao");

            }

        }

        assertNotSame(new BuilderA().build(), new BuilderA().build());
        assertEquals(new BuilderA().build(), new BuilderA().build());

    }

}