package rocks.gioac96.veronica.http;

import java.io.IOException;

public interface ExceptionHandler {

    default Response handle(Exception e) {

        return Response.builder()
            .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("")
            .build();

    }

    default void handleExchangeException(IOException e) {}

}
