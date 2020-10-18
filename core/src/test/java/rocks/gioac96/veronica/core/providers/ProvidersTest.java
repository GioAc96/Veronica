package rocks.gioac96.veronica.core.providers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ProvidersTest {

    private static class GreetingProvider extends ConfigurableProvider<String> {

        private String greeting = null;

        public GreetingProvider italian() {

            greeting = "Ciao";
            return this;

        }
        public GreetingProvider english() {

            greeting = "Hello";
            return this;

        }

        @Override
        protected boolean isValid() {

            return super.isValid()
                && greeting != null;

        }

        @Override
        protected String instantiate() {

            return new String(greeting);

        }

    }

    private static class ItalianGreetingProvider extends GreetingProvider {

        @Override
        protected void configure() {

            italian();

            super.configure();

        }

    }
    private static class ItalianGreetingProviderSingleton extends ItalianGreetingProvider implements Singleton {

    }

    private class EnglishGreetingProvider extends GreetingProvider {

        @Override
        protected void configure() {

            english();

            super.configure();

        }

    }

    @Test
    void testNotValidFails() {

        assertThrows(
            CreationException.class,
            () -> new GreetingProvider().provide()
        );

    }

    @Test
    void testValidSucceeds() {

        new GreetingProvider().italian().provide();
        new GreetingProvider().english().provide();

    }

    @Test
    void testSingletonSingleInstance() {

        assertSame(
            new ItalianGreetingProviderSingleton().provide(),
            new ItalianGreetingProviderSingleton().provide()
        );

    }

    @Test
    void testMultipleInstances() {

        String g1 = new EnglishGreetingProvider().provide();
        String g2 = new EnglishGreetingProvider().provide();

        assertNotSame(g1, g2);
        assertEquals(g1, g2);

    }

    @Test
    void testReconfigure() {

        assertEquals(
            "Ciao",
            new GreetingProvider()
                .italian()
                .provide()
        );
        assertEquals(
            "Hello",
            new GreetingProvider()
                .english()
                .provide()
        );

    }

}