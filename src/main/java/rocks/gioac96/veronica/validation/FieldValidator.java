package rocks.gioac96.veronica.validation;

import java.util.PriorityQueue;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.util.HasPriority;
import rocks.gioac96.veronica.util.PriorityEntry;

/**
 * Field validator.
 */
@Getter
public class FieldValidator {

    private final PriorityQueue<PriorityEntry<ValidationRule>> validationRules;
    private final Boolean nullable;

    protected FieldValidator(FieldValidatorBuilder b) {

        this.validationRules = b.validationRules;
        this.nullable = b.nullable;

    }

    public static FieldValidatorBuilder builder() {

        class FieldValidatorBuilderImpl extends FieldValidatorBuilder implements BuildsMultipleInstances {

        }

        return new FieldValidatorBuilderImpl();

    }


    public void validateField(String fieldName, String fieldValue) throws ValidationException {

        if (fieldValue == null && !nullable) {

            ValidationFailureData failureData = ValidationFailureData.builder()
                .fieldName(fieldName)
                .failureReason(CommonValidationFailureReasons.isNull())
                .build();

            throw new ValidationException(failureData);

        } else {

            for (PriorityEntry<ValidationRule> validationRuleEntry : validationRules) {

                validationRuleEntry.getValue().validate(fieldName, fieldValue);

            }

        }

    }

    public abstract static class FieldValidatorBuilder extends Builder<FieldValidator> {

        private final PriorityQueue<PriorityEntry<ValidationRule>> validationRules = new PriorityQueue<>();
        private Boolean nullable = false;

        public FieldValidatorBuilder validationRule(@NonNull ValidationRule validationRule) {

            this.validationRules.add(new PriorityEntry<>(validationRule));
            return this;

        }

        public FieldValidatorBuilder validationRule(@NonNull ValidationRule validationRule, int priority) {

            this.validationRules.add(new PriorityEntry<>(validationRule, priority));
            return this;

        }

        public FieldValidatorBuilder validationRule(@NonNull Provider<ValidationRule> validationRuleProvider) {

            if (validationRuleProvider instanceof HasPriority) {

                return this.validationRule(
                    validationRuleProvider.provide(),
                    ((HasPriority) validationRuleProvider).getPriority()
                );

            } else {

                return this.validationRule(validationRuleProvider.provide());

            }

        }


        public FieldValidatorBuilder nullable(@NonNull Boolean nullable) {

            this.nullable = nullable;
            return this;

        }

        public FieldValidatorBuilder nullable(@NonNull Provider<Boolean> nullable) {

            return nullable(nullable.provide());

        }

        public FieldValidatorBuilder nullable() {

            return nullable(true);

        }

        public FieldValidatorBuilder notNullable() {

            return nullable(false);

        }

        @Override
        protected boolean isValid() {

            return super.isValid()
                && validationRules != null
                && nullable != null;

        }

        @Override
        protected FieldValidator instantiate() {

            return new FieldValidator(this);

        }

    }

}
