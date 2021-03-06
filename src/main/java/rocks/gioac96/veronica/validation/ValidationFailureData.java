package rocks.gioac96.veronica.validation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * Validation failure data, consisting of the name of the field that has an invalid value, and the
 * reason of the failure.
 */
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ValidationFailureData {

    @Getter
    @NonNull
    private final ValidationFailureReason failureReason;

    @Getter
    @NonNull
    private final String fieldName;

}
