package rocks.gioac96.veronica.validation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.providers.ConfigurableProvider;
import rocks.gioac96.veronica.providers.Provider;

/**
 * Reasons for validation failure.
 */
@Getter
@EqualsAndHashCode
public class ValidationFailureReason {

    private final String message;

    protected ValidationFailureReason(ValidationFailureReasonBuilder b) {

        this.message = b.message;

    }

    public static ValidationFailureReasonBuilder builder() {

        return new ValidationFailureReasonBuilder();

    }

    public static class ValidationFailureReasonBuilder extends ConfigurableProvider<ValidationFailureReason> {

        private String message;

        public ValidationFailureReasonBuilder message(@NonNull String message) {

            this.message = message;
            return this;

        }

        public ValidationFailureReasonBuilder message(@NonNull Provider<String> messageProvider) {

            return message(messageProvider.provide());

        }

        @Override
        protected boolean isValid() {

            return message != null;

        }

        @Override
        protected ValidationFailureReason instantiate() {

            return new ValidationFailureReason(this);

        }

    }

}
