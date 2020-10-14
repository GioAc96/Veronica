package rocks.gioac96.veronica.common.validation_rules;

import lombok.NonNull;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;
import rocks.gioac96.veronica.validation.ValidationFailureReason;
import rocks.gioac96.veronica.validation.ValidationRule;

public class RegexRule
    extends ValidationRuleBuilderWithConstantFailureReason
    implements BuildsMultipleInstances {

    protected String pattern;

    public RegexRule pattern(@NonNull String pattern) {

        this.pattern = pattern;
        return this;

    }

    public RegexRule pattern(@NonNull Provider<String> patternProvider) {

        return pattern(patternProvider.provide());

    }

    @Override
    protected void configure() {

        super.configure();

        failureReason(ValidationFailureReason.builder()
            .message("value does not match the expected pattern")
            .build()
        );

    }

    @Override
    protected boolean isValid() {

        return super.isValid()
            && pattern != null;

    }

    @Override
    protected ValidationRule instantiate() {

        return (fieldName, fieldValue) -> {

            if (! fieldValue.matches(pattern)) {

                throw new ValidationException(ValidationFailureData.builder()
                    .failureReason(failureReason)
                    .fieldName(fieldName)
                    .build());

            }

        };

    }

}
