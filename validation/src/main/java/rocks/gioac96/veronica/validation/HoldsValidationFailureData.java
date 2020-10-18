package rocks.gioac96.veronica.validation;

public interface HoldsValidationFailureData {

    void addValidationFailure(String fieldName, ValidationFailureReason validationFailureReason);

}
