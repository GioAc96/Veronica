package rocks.gioac96.veronica.routing.pipeline.validation;

import java.util.Collection;
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
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "unused"})
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType", "unused"})
    public abstract static class FieldValidatorBuilder<
        C extends FieldValidator,
        B extends FieldValidatorBuilder<C, B>
        > {

        @NonNull
        private final PrioritySet<ValidationRule> validationRules = new PrioritySet<>();

        @NonNull
        private Boolean nullable = false;

        @SuppressWarnings("unused")
        public B validationRule(ValidationRule validationRule) {

            this.validationRules.add(validationRule);

            return self();

        }

        @SuppressWarnings("unused")
        public B validationRules(Collection<ValidationRule> validationRules) {

            this.validationRules.addAll(validationRules);

            return self();

        }

        @SuppressWarnings("unused")
        public B validationRules(PrioritySet<ValidationRule> validationRules) {

            this.validationRules.addAll(validationRules);

            return self();

        }


        public B nullable(@NonNull Boolean nullable) {

            this.nullable = nullable;
            return self();

        }

        @SuppressWarnings("unused")
        public B nullable() {

            return nullable(true);

        }

        @SuppressWarnings("unused")
        public B notNullable() {

            return nullable(false);

        }

        protected abstract B self();

        @SuppressWarnings("unused")
        public abstract C build();

    }

    @SuppressWarnings("unused")
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
