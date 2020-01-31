package org.gioac96.veronica.http;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Builder
public class Response {

    @Getter
    @Setter
    protected String body;

    @Getter
    @Setter
    @NonNull
    protected HttpStatus httpStatus;


}
