package org.gioac96.veronica.routing.pipeline.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ValidationFailureData {

    @Getter
    private final ValidationFailureReason failureReason;

    @Getter
    private final String fieldName;

}
