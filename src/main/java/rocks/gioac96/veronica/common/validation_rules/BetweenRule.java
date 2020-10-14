package rocks.gioac96.veronica.common.validation_rules;

import lombok.NonNull;
import rocks.gioac96.veronica.providers.Provider;

public class BetweenRule extends NumericPredicateRule {

    protected Double minValue;
    protected Double maxValue;

    public BetweenRule minValue(@NonNull Double minValue) {

        this.minValue = minValue;
        return this;

    }

    public BetweenRule minValue(@NonNull Provider<Double> minValueProvider) {

        return minValue(minValueProvider.provide());

    }


    public BetweenRule maxValue(@NonNull Double maxValue) {

        this.maxValue = maxValue;
        return this;

    }

    public BetweenRule maxValue(@NonNull Provider<Double> maxValueProvider) {

        return maxValue(maxValueProvider.provide());

    }

    @Override
    protected void configure() {

        super.configure();

        predicate(value -> value <= maxValue && value >= minValue);

    }


}
