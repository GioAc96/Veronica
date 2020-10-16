package rocks.gioac96.veronica.common.validation_rules;

import lombok.NonNull;
import rocks.gioac96.veronica.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.validation.ValidationFailureReason;
import rocks.gioac96.veronica.validation.ValidationRule;

public abstract class ValidationRuleBuilderWithConstantFailureReason
    extends Builder<ValidationRule> {

    protected ValidationFailureReason failureReason;

    public ValidationRuleBuilderWithConstantFailureReason failureReason(@NonNull ValidationFailureReason failureReason) {

        this.failureReason = failureReason;
        return this;

    }

    public ValidationRuleBuilderWithConstantFailureReason failureReason(@NonNull Provider<ValidationFailureReason> failureReasonProvider) {

        return failureReason(failureReasonProvider.provide());

    }

    @Override
    protected void configure() {

        if (failureReason == null) {

            failureReason = CommonValidationFailureReasons.generic();

        }

        super.configure();

    }

    @Override
    protected boolean isValid() {

        return super.isValid()
            && failureReason != null;

    }

}
