package org.gioac96.veronica.routing.pipeline.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Validation failure data, consisting of the name of the field that has an invalid value, and the
 * reason of the failure.
 */
@AllArgsConstructor
public class ValidationFailureData {

    @Getter
    private final ValidationFailureReason failureReason;

    @Getter
    private final String fieldName;

}
