package org.gioac96.veronica.routing.pipeline.validation;

import lombok.Getter;
import lombok.Setter;
import org.gioac96.veronica.util.PriorityList;

public class FieldValidator {

    @Getter
    private PriorityList<ValidationRule> validationRules;

    @Getter
    @Setter
    private boolean nullable;

    public FieldValidator() {

        validationRules = new PriorityList<>();
        nullable = false;

    }

    public FieldValidator(boolean nullable) {

        this.nullable = nullable;

    }


    public void validateField(String fieldName, String fieldValue) throws ValidationException {

        if (nullable && (fieldValue == null || fieldValue == "")) {
            return;
        }

        for (ValidationRule validationRule : validationRules) {

            validationRule.validate(fieldName, fieldValue);

        }

    }

}
