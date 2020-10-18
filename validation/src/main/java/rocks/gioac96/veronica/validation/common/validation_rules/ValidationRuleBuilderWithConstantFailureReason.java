package rocks.gioac96.veronica.validation.common.validation_rules;

import lombok.NonNull;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.Provider;
import rocks.gioac96.veronica.validation.ValidationFailureReason;
import rocks.gioac96.veronica.validation.ValidationRule;
import rocks.gioac96.veronica.validation.common.CommonValidationFailureReasons;

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
