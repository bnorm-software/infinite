package com.bnorm.infinite;

import java.util.function.Supplier;

/**
 * A factory interface for transitions.
 *
 * @param <S> the class type of the states.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.0.0
 */
public interface TransitionFactory<S, C> {

    /**
     * Returns the default internal state factory.  This is the internal state base constructor.
     *
     * @param <S> the class type of the states.
     * @param <C> the class type of the context.
     * @return default internal state factory.
     */
    static <S, C> TransitionFactory<S, C> getDefault() {
        return TransitionBase::new;
    }

    /**
     * Creates a transition from the specified transition.  This is a snapshot clone of the transition.  The returned
     * transition will have the default guard and the destination will be a constant representation of the specified
     * transition's next destination.
     *
     * @param transition the transition of which to make a snapshot clone.
     * @return a transition.
     */
    default Transition<S, C> create(Transition<S, C> transition) {
        return create(transition.getSource(), transition.getDestination());
    }

    /**
     * Creates a transition from the specified source and destination states.
     *
     * @param source the source state of the transition.
     * @param destination the destination state of the transition.
     * @return a transition.
     */
    default Transition<S, C> create(S source, S destination) {
        return create(source, () -> destination);
    }

    /**
     * Creates a transition from the specified source and destination states.
     *
     * @param source the source state of the transition.
     * @param destination the destination state of the transition.
     * @return a transition.
     */
    default Transition<S, C> create(S source, Supplier<S> destination) {
        return create(source, destination, TransitionGuard.none());
    }

    /**
     * Creates a transition from the specified source and destination states and the transition guard.
     *
     * @param source the source state of the transition.
     * @param destination the destination state of the transition.
     * @param guard the guard for the transition.
     * @return a transition.
     */
    default Transition<S, C> create(S source, S destination, TransitionGuard<C> guard) {
        return create(source, () -> destination, guard);
    }

    /**
     * Creates a transition from the specified source and destination states and the transition guard.
     *
     * @param source the source state of the transition.
     * @param destination the destination state supplier of the transition.
     * @param guard the guard for the transition.
     * @return a transition.
     */
    Transition<S, C> create(S source, Supplier<S> destination, TransitionGuard<C> guard);
}
