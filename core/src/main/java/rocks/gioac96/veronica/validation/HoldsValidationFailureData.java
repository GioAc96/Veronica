package rocks.gioac96.veronica.validation;

import java.util.Map;

public interface HoldsValidationFailureData {

    void addValidationFailure(String fieldName, ValidationFailureReason validationFailureReason);

}
