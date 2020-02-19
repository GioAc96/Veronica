package rocks.gioac96.veronica.routing.pipeline.validation;

import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.util.PrioritySet;

/**
 * Field validator.
 * Builder is extensible with lombok's {@link lombok.experimental.SuperBuilder}.
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

    protected FieldValidator(FieldValidatorBuilder<?, ?> b) {

        this.validationRules = b.validationRules;
        this.nullable = b.nullable;

    }

    @Generated
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static FieldValidatorBuilder<?, ?> builder() {

        return new FieldValidatorBuilderImpl();

    }

    /**
     * Validates a field by enforcing all of the field's validation rules.
     *
     * @param fieldName  name of the field to validate
     * @param fieldValue value of the field to validate
     * @throws ValidationException on validation failure
     */
    public void validateField(String fieldName, String fieldValue) throws ValidationException {

        if (fieldValue == null || fieldValue.equals("")) {

            if (!nullable) {

                ValidationFailureData failureData = new ValidationFailureData(
                    CommonValidationFailureReason.NOT_PRESENT,
                    fieldName
                );

                ValidationFailureResponse failureResponse = ValidationFailureResponse.builder()
                    .validationFailureData(failureData)
                    .build();

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

    @Generated
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public abstract static class FieldValidatorBuilder<
        C extends FieldValidator,
        B extends FieldValidatorBuilder<C, B>
        > {

        private @NonNull PrioritySet<ValidationRule> validationRules = new PrioritySet<>();
        private @NonNull Boolean nullable = false;

        public B validationRules(ValidationRule... validationRules) {

            Collections.addAll(this.validationRules, validationRules);

            return self();

        }

        public B validationRules(Collection<ValidationRule> validationRules) {

            this.validationRules.addAll(validationRules);

            return self();

        }


        public B nullable(@NonNull Boolean nullable) {

            this.nullable = nullable;
            return self();

        }

        public B nullable() {

            return nullable(true);

        }

        public B notNullable() {

            return nullable(false);

        }

        protected abstract B self();

        public abstract C build();

        public String toString() {
            return
                "FieldValidator.FieldValidatorBuilder(validationRules=" + this.validationRules
                    + ", nullable=" + this.nullable + ")";
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class FieldValidatorBuilderImpl extends FieldValidatorBuilder<
        FieldValidator,
        FieldValidatorBuilderImpl
        > {

        protected FieldValidator.FieldValidatorBuilderImpl self() {

            return this;

        }

        public FieldValidator build() {

            return new FieldValidator(this);

        }

    }
}
