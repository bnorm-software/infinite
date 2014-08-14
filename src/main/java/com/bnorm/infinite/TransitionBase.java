package com.bnorm.infinite;

import java.util.function.Supplier;

/**
 * The base implementation of a transition.
 *
 * @param <S> the class type of the states.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.0.0
 */
public class TransitionBase<S, C> implements Transition<S, C> {

    /** The source state of the transition. */
    private final S source;

    /** The destination state supplier of the transition. */
    private final Supplier<S> destination;

    /** The conditional nature of the transition. */
    private final TransitionGuard<C> guard;

    /**
     * Constructs a new transition from the specified source and destination states and the transition guard.
     *
     * @param source the source state of the transition.
     * @param destination the destination state supplier of the transition.
     * @param guard the guard for the transition.
     */
    protected TransitionBase(S source, Supplier<S> destination, TransitionGuard<C> guard) {
        this.source = source;
        this.destination = destination;
        this.guard = guard;
    }

    @Override
    public S getSource() {
        return source;
    }

    @Override
    public S getDestination() {
        return destination.get();
    }

    @Override
    public TransitionGuard<C> getGuard() {
        return guard;
    }

    @Override
    public String toString() {
        return "TransitionBase[" + getSource() + "->" + getDestination() + "]";
    }
}
