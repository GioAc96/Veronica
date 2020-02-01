package org.gioac96.veronica.routing.pipeline.validation;

public interface ValidationRule {

    void validate(String fieldName, String fieldValue) throws ValidationException;

}
