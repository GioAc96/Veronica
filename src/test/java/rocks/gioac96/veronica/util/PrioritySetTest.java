package rocks.gioac96.veronica.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PrioritySetTest {

    @Test
    protected void testAddAll() {

        PrioritySet<String> a = new PrioritySet<>();

        PrioritySet<String> b = new PrioritySet<String>() {{
            add("test");
            add("ciao");
        }};

        a.addAll(b);

        assertEquals(b.size(), a.size());

        for (String s : b) {

            assertTrue(a.contains(s));

        }

    }

}