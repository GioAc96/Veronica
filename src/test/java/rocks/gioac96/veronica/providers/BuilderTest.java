package rocks.gioac96.veronica.providers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class BuilderTest {

    @Test
    void testSingleInstance() {

        class SingleInstanceBuilder extends GreetingBuilder  {
        }

        assertSame(new SingleInstanceBuilder().provide(), new SingleInstanceBuilder().provide());

    }

    @Test
    void testMultipleInstances() {

        class MultipleInstancesBuilder extends GreetingBuilder  {
        }

        assertNotSame(new MultipleInstancesBuilder().provide(), new MultipleInstancesBuilder().provide());
        assertEquals(new MultipleInstancesBuilder().provide(), new MultipleInstancesBuilder().provide());

    }

    @Test
    void testMultipleConfigurations() {

        abstract class RootBuilder extends ConfigurableProvider<String>  {

            protected String value = "";

            @Override
            protected void configure() {

                value += "RootBuilder";

                super.configure();

            }

            @Override
            protected String instantiate() {
                return value;
            }
        }

        class ChildBuilder extends RootBuilder {

            @Override
            protected void configure() {

                value += "ChildBuilder";

                super.configure();

            }

        }

        assertEquals("ChildBuilderRootBuilder", new ChildBuilder().provide());

    }

    private static abstract class GreetingBuilder extends ConfigurableProvider<String> {

        @Override
        protected String instantiate() {
            return new String("Ciao");
        }

    }

}