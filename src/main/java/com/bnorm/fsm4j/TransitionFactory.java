package com.bnorm.fsm4j;

import java.util.Optional;
import java.util.function.BooleanSupplier;

/**
 * @param <S> the class type of the states.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public interface TransitionFactory<S> {

    /**
     * Returns the default internal state factory.  This is the internal state base constructor.
     *
     * @return default internal state factory.
     */
    static <S> TransitionFactory<S> getDefault() {
        return TransitionBase::new;
    }

    /**
     * Creates a transition from the specified source and destination states.
     *
     * @param source the source state of the transition.
     * @param destination the destination state of the transition.
     * @return a transition.
     */
    default TransitionBase<S> create(S source, S destination) {
        return create(source, destination, Optional.empty());
    }

    /**
     * Creates a transition from the specified source and destination states and the conditional supplier.
     *
     * @param source the source state of the transition.
     * @param destination the destination state of the transition.
     * @param conditional the conditional nature of the transition.
     * @return a transition.
     */
    default TransitionBase<S> create(S source, S destination, BooleanSupplier conditional) {
        return create(source, destination, Optional.of(conditional));
    }

    /**
     * Creates a transition from the specified source and destination states and the optional conditional supplier.
     *
     * @param source the source state of the transition.
     * @param destination the destination state of the transition.
     * @param conditional the conditional nature of the transition.
     * @return a transition.
     */
    TransitionBase<S> create(S source, S destination, Optional<BooleanSupplier> conditional);
}
