package com.bnorm.infinite;

/**
 * A factory interface for transitions.
 *
 * @param <S> the class type of the states.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.0.1
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
     * Creates a transition from the specified source and destination states.
     *
     * @param source the source state of the transition.
     * @param destination the destination state of the transition.
     * @return a transition.
     */
    default Transition<S, C> create(S source, S destination) {
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
    Transition<S, C> create(S source, S destination, TransitionGuard<C> guard);
}
