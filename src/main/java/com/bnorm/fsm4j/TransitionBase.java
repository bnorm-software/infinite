package com.bnorm.fsm4j;

import java.util.Optional;
import java.util.function.BooleanSupplier;

/**
 * The base implementation of a transition.
 *
 * @param <S> the class type of the states.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public class TransitionBase<S> implements Transition<S> {

    /** The source state of the transition. */
    private final S source;

    /** The destination state of the transition. */
    private final S destination;

    /** The conditional nature of the transition. */
    private final Optional<BooleanSupplier> conditional;

    /**
     * Constructs a new transition from the specified source and destination states.
     *
     * @param source the source state of the transition.
     * @param destination the destination state of the transition.
     */
    protected TransitionBase(S source, S destination) {
        this(source, destination, Optional.empty());
    }

    /**
     * Constructs a new transition from the specified source and destination states and the conditional supplier.
     *
     * @param source the source state of the transition.
     * @param destination the destination state of the transition.
     * @param conditional the conditional nature of the transition.
     */
    protected TransitionBase(S source, S destination, BooleanSupplier conditional) {
        this(source, destination, Optional.of(conditional));
    }

    /**
     * Constructs a new transition from the specified source and destination states and the optional conditional
     * supplier.
     *
     * @param source the source state of the transition.
     * @param destination the destination state of the transition.
     * @param conditional the conditional nature of the transition.
     */
    protected TransitionBase(S source, S destination, Optional<BooleanSupplier> conditional) {
        this.source = source;
        this.destination = destination;
        this.conditional = conditional;
    }

    @Override
    public S getSource() {
        return source;
    }

    @Override
    public S getDestination() {
        return destination;
    }

    @Override
    public boolean allowed() {
        return !conditional.isPresent() || conditional.get().getAsBoolean();
    }

    @Override
    public String toString() {
        return "TransitionBase[" + source + "->" + destination + "]";
    }
}
