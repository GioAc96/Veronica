package rocks.gioac96.veronica.common.validation_rules;

import lombok.NonNull;
import rocks.gioac96.veronica.providers.Provider;

public class MaxRule extends NumericPredicateRule {
    
    protected Double maxValue;

    public MaxRule maxValue(@NonNull Double maxValue) {

        this.maxValue = maxValue;
        return this;

    }

    public MaxRule maxValue(@NonNull Provider<Double> maxValueProvider) {

        return maxValue(maxValueProvider.provide());

    }

    @Override
    protected void configure() {

        super.configure();

        predicate(value -> value <= maxValue);

    }

}
