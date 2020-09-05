package rocks.gioac96.veronica.validation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.DeclaresPriority;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.util.PrioritySet;

/**
 * Field validator.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FieldValidator {

    @Getter
    @Setter
    @NonNull
    private PrioritySet<ValidationRule> validationRules;

    @Getter
    @Setter
    @NonNull
    private Boolean nullable;

    protected FieldValidator(FieldValidatorBuilder b) {

        this.validationRules = b.validationRules;
        this.nullable = b.nullable;

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "unused"})
    public static FieldValidatorBuilder builder() {

        class FieldValidatorBuilderImpl extends FieldValidatorBuilder implements BuildsMultipleInstances {

        }

        return new FieldValidatorBuilderImpl();

    }

    /**
     * Validates a field by enforcing all of the field's validation rules.
     *
     * @param fieldName  name of the field to validate
     * @param fieldValue value of the field to validate
     * @throws ValidationException on validation failure
     */
    @SuppressWarnings("unused")
    public void validateField(String fieldName, String fieldValue) throws ValidationException {

        if (fieldValue == null || fieldValue.equals("")) {

            if (!nullable) {

                ValidationFailureData failureData = new ValidationFailureData(
                    CommonValidationFailureReason.NOT_PRESENT,
                    fieldName
                );

                throw ValidationException.builder()
                    .validationFailureData(failureData)
                    .build();

            }

        } else {

            for (ValidationRule validationRule : validationRules) {

                validationRule.validate(fieldName, fieldValue);

            }

        }

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType", "unused"})
    public abstract static class FieldValidatorBuilder extends Builder<FieldValidator> {

        @NonNull
        private final PrioritySet<ValidationRule> validationRules = new PrioritySet<>();

        @NonNull
        private Boolean nullable = false;

        public FieldValidatorBuilder validationRule(@NonNull ValidationRule validationRule) {

            this.validationRules.add(validationRule);

            return this;

        }

        public FieldValidatorBuilder validationRule(@NonNull ValidationRule validationRule, int priority) {

            this.validationRules.add(validationRule, priority);

            return this;

        }

        public FieldValidatorBuilder validationRule(@NonNull Provider<ValidationRule> validationRuleProvider) {

            if (validationRuleProvider instanceof DeclaresPriority) {

                return validationRule(
                    validationRuleProvider.provide(),
                    ((DeclaresPriority) validationRuleProvider).priority()
                );

            } else {

                return validationRule(validationRuleProvider.provide());

            }

        }

        public FieldValidatorBuilder nullable(@NonNull Boolean nullable) {

            this.nullable = nullable;
            return this;

        }

        public FieldValidatorBuilder nullable() {

            return nullable(true);

        }

        public FieldValidatorBuilder notNullable() {

            return nullable(false);

        }

        @Override
        protected FieldValidator instantiate() {

            return new FieldValidator(this);

        }

    }
    
}
