package rocks.gioac96.veronica.validation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.Provider;

/**
 * Validation failure data, consisting of the name of the field that has an invalid value, and the
 * reason of the failure.
 */
@Getter
@EqualsAndHashCode
public class ValidationFailureData {

    private final ValidationFailureReason failureReason;
    private final String fieldName;

    protected ValidationFailureData(ValidationFailureDataBuilder b) {

        this.failureReason = b.failureReason;
        this.fieldName = b.fieldName;

    }

    public static ValidationFailureDataBuilder builder() {

        class ValidationFailureDataBuilderImpl
            extends ValidationFailureDataBuilder
            implements BuildsMultipleInstances {

        }

        return new ValidationFailureDataBuilderImpl();

    }

    public abstract static class ValidationFailureDataBuilder extends Builder<ValidationFailureData> {

        protected ValidationFailureReason failureReason;
        protected String fieldName;

        public ValidationFailureDataBuilder failureReason(@NonNull ValidationFailureReason failureReason) {

            this.failureReason = failureReason;
            return this;

        }

        public ValidationFailureDataBuilder failureReason(@NonNull Provider<ValidationFailureReason> failureReasonProvider) {

            return failureReason(failureReasonProvider.provide());

        }

        public ValidationFailureDataBuilder fieldName(@NonNull String fieldName) {

            this.fieldName = fieldName;
            return this;

        }

        public ValidationFailureDataBuilder fieldName(@NonNull Provider<String> fieldNameProvider) {

            return fieldName(fieldNameProvider.provide());

        }

        @Override
        protected boolean isValid() {

            return super.isValid()
                && failureReason != null
                && fieldName != null;

        }

        @Override
        protected ValidationFailureData instantiate() {

            return new ValidationFailureData(this);

        }

    }

}
