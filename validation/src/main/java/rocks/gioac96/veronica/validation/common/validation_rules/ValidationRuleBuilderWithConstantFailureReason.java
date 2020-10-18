package rocks.gioac96.veronica.core.validation.validation_rules;

import lombok.NonNull;
import rocks.gioac96.veronica.core.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.providers.ConfigurableProvider;
import rocks.gioac96.veronica.providers.Provider;
import validation.ValidationFailureReason;
import validation.ValidationRule;

public abstract class ValidationRuleBuilderWithConstantFailureReason
    extends ConfigurableProvider<ValidationRule> {

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
