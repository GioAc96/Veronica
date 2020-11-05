package rocks.gioac96.veronica.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ResponseTest {

    @Test
    void testStringBody() {

        String body = "this is some body";

        Response response = Response.builder()
            .body(body)
            .provide();

        assertSame(body, response.getBodyString());
        assertArrayEquals(body.getBytes(), response.getBodyBytes());

    }

    @Test
    void testBytesBody() {

        byte[] body = new byte[]{0x00, 0x01, 0x02};

        Response response = Response.builder()
            .body(body)
            .provide();

        assertNull(response.getBodyString());
        assertSame(body, response.getBodyBytes());

    }

}