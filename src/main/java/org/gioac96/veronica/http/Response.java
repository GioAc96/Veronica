package org.gioac96.veronica.http;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Builder
public class Response {

    @Getter
    @Setter
    private String body;

    @Getter
    @Setter
    @NonNull
    private HttpStatus httpStatus;


}
