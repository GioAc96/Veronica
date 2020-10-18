package rocks.gioac96.veronica.validation;

import java.util.PriorityQueue;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.providers.ConfigurableProvider;
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

        return new FieldValidatorBuilder();

    }

    public void validateField(String fieldValue) throws ValidationException {

        if (fieldValue == null && !nullable) {

            throw new ValidationException(CommonValidationFailureReasons.isNull());

        } else {

            for (PriorityEntry<ValidationRule> validationRuleEntry : validationRules) {

                validationRuleEntry.getValue().validate(fieldValue);

            }

        }

    }

    public static class FieldValidatorBuilder extends ConfigurableProvider<FieldValidator> {

        protected PriorityQueue<PriorityEntry<ValidationRule>> validationRules = new PriorityQueue<>();
        protected boolean nullable;

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

        public FieldValidatorBuilder nullable(@NonNull Provider<Boolean> nullableProvider) {

            return nullable(nullableProvider.provide());

        }

        public FieldValidatorBuilder nullable() {

            return nullable(true);

        }

        public FieldValidatorBuilder notNullable() {

            return nullable(false);

        }

        @Override
        protected boolean isValid() {

            return validationRules != null;

        }

        @Override
        protected FieldValidator instantiate() {

            return new FieldValidator(this);

        }

    }

}
