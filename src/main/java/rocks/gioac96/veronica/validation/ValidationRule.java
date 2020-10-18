package rocks.gioac96.veronica.validation;

/**
 * Field validation rule.
 */
public interface ValidationRule {

    void validate(String fieldValue) throws ValidationException;

}
