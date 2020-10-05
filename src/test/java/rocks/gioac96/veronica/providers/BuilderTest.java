package rocks.gioac96.veronica.providers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BuilderTest {


    private static abstract class GreetingBuilder extends Builder<String> {

        @Override
        protected String instantiate() {
            return new String("Ciao");
        }

    }

    @Test
    void testSingleInstance() {

        class SingleInstanceBuilder extends GreetingBuilder implements BuildsSingleInstance{}

        assertSame(new SingleInstanceBuilder().build(), new SingleInstanceBuilder().build());

    }

    @Test
    void testMultipleInstances() {

        class MultipleInstancesBuilder extends GreetingBuilder implements BuildsMultipleInstances{}

        assertNotSame(new MultipleInstancesBuilder().build(), new MultipleInstancesBuilder().build());

    }

    @Test
    void testMultipleConfigurations() {

        abstract class RootBuilder extends Builder<String> implements BuildsMultipleInstances {

            protected String value = "";

            @Override
            protected void configure() {

                super.configure();

                value += "RootBuilder";

            }

            @Override
            protected String instantiate() {
                return new String(value);
            }
        }

        class ChildBuilder extends RootBuilder {

            @Override
            protected void configure() {

                super.configure();

                value += "ChildBuilder";

            }

        }

        assertEquals("RootBuilderChildBuilder", new ChildBuilder().build());

    }

}