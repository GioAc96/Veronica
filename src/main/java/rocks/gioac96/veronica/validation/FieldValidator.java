package rocks.gioac96.veronica.validation;

import java.util.PriorityQueue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.util.HasPriority;
import rocks.gioac96.veronica.util.PriorityEntry;

/**
 * Field validator.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FieldValidator {

    private PriorityQueue<PriorityEntry<ValidationRule>> validationRules;
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

            for (PriorityEntry<ValidationRule> validationRuleEntry : validationRules) {

                validationRuleEntry.getValue().validate(fieldName, fieldValue);

            }

        }

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType", "unused"})
    public abstract static class FieldValidatorBuilder extends Builder<FieldValidator> {

        private final PriorityQueue<PriorityEntry<ValidationRule>> validationRules = new PriorityQueue<>();
        private Boolean nullable = false;

        public FieldValidatorBuilder validationRules(@NonNull ValidationRule validationRules) {

            this.validationRules.add(new PriorityEntry<>(validationRules));
            return this;

        }

        public FieldValidatorBuilder validationRules(@NonNull ValidationRule validationRules, int priority) {

            this.validationRules.add(new PriorityEntry<>(validationRules, priority));
            return this;

        }

        public FieldValidatorBuilder validationRules(@NonNull Provider<ValidationRule> validationRulesProvider) {

            if (validationRulesProvider instanceof HasPriority) {

                return this.validationRules(
                    validationRulesProvider.provide(),
                    ((HasPriority) validationRulesProvider).getPriority()
                );

            } else {

                return this.validationRules(validationRulesProvider.provide());

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
