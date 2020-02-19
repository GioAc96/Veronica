package rocks.gioac96.veronica.routing.pipeline.validation;

import lombok.Getter;

/**
 * Common reasons for validation failure.
 */
public enum CommonValidationFailureReason implements ValidationFailureReason {

    NOT_IN_ARRAY("field value is not in array of possible values"),
    NOT_BOOLEAN("field value must be either true or false"),
    PATTERN_NOT_MATCHED("field does not match pattern"),
    INVALID_EMAIL("email is not valid"),
    OUT_OF_RANGE("field value is out of range"),
    NOT_NUMERIC("the field value is not numeric"),
    NOT_PRESENT("the field value can not be null or unspecified");

    @Getter
    private final String message;

    CommonValidationFailureReason(String message) {

        this.message = message;

    }

}
