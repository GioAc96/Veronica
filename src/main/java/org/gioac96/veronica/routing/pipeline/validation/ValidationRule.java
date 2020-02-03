package org.gioac96.veronica.routing.pipeline.validation;

/**
 * Field validation rule.
 */
public interface ValidationRule {

    void validate(String fieldName, String fieldValue) throws ValidationException;

}
