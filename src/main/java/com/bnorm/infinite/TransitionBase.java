package com.bnorm.infinite;

import java.util.function.Supplier;

/**
 * The base implementation of a transition.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.0.0
 */
public class TransitionBase<S, E, C> implements Transition<S, E, C> {

    /** The source state of the transition. */
    private final S source;

    /** The destination state supplier of the transition. */
    private final Supplier<S> destination;

    /** The conditional nature of the transition. */
    private final TransitionGuard<C> guard;

    /** The action to perform during the transition. */
    private final Action<S, E, C> action;

    /**
     * Constructs a new transition from the specified source and destination states and the transition guard.
     *
     * @param source the source state of the transition.
     * @param destination the destination state supplier of the transition.
     * @param guard the guard for the transition.
     * @param action the action to perform during the transition.
     */
    protected TransitionBase(S source, Supplier<S> destination, TransitionGuard<C> guard, Action<S, E, C> action) {
        this.source = source;
        this.destination = destination;
        this.guard = guard;
        this.action = action;
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
    public Action<S, E, C> getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "TransitionBase[" + getSource() + "->" + getDestination() + "]";
    }
}
