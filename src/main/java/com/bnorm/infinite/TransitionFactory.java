package com.bnorm.infinite;

/**
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public interface TransitionFactory {

    /**
     * Returns the default internal state factory.  This is the internal state base constructor.
     *
     * @return default internal state factory.
     */
    static TransitionFactory getDefault() {
        return TransitionBase::new;
    }

    /**
     * Creates a transition from the specified source and destination states.
     *
     * @param source the source state of the transition.
     * @param destination the destination state of the transition.
     * @param <S> the class type of the states.
     * @param <C> the class type of the context.
     * @return a transition.
     */
    default <S, C> TransitionBase<S, C> create(S source, S destination) {
        return create(source, destination, TransitionGuard.none());
    }

    /**
     * Creates a transition from the specified source and destination states and the transition guard.
     *
     * @param source the source state of the transition.
     * @param destination the destination state of the transition.
     * @param guard the guard for the transition.
     * @param <S> the class type of the states.
     * @param <C> the class type of the context.
     * @return a transition.
     */
    <S, C> TransitionBase<S, C> create(S source, S destination, TransitionGuard<C> guard);
}
