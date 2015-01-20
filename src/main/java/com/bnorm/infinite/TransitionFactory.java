package com.bnorm.infinite;

import java.util.function.Supplier;

/**
 * A factory interface for transitions.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.0.0
 */
public interface TransitionFactory<S, E, C> {

    /**
     * Returns the default internal state factory.  This is the internal state base constructor.
     *
     * @param <S> the class type of the states.
     * @param <C> the class type of the context.
     * @return default internal state factory.
     */
    static <S, E, C> TransitionFactory<S, E, C> getDefault() {
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
    default Transition<S, E, C> create(Transition<S, E, C> transition) {
        return create(transition.getSource(), transition.getDestination(), transition.getAction());
    }

    /**
     * Creates a transition from the specified source and destination states.
     *
     * @param source the source state of the transition.
     * @param destination the destination state of the transition.
     * @return a transition.
     */
    default Transition<S, E, C> create(S source, S destination) {
        return create(source, () -> destination);
    }

    /**
     * Creates a transition from the specified source and destination states.
     *
     * @param source the source state of the transition.
     * @param destination the destination state of the transition.
     * @return a transition.
     */
    default Transition<S, E, C> create(S source, Supplier<S> destination) {
        return create(source, destination, TransitionGuard.none(), Action.noAction());
    }

    /**
     * Creates a transition from the specified source and destination states and the transition guard.
     *
     * @param source the source state of the transition.
     * @param destination the destination state of the transition.
     * @param guard the guard for the transition.
     * @return a transition.
     */
    default Transition<S, E, C> create(S source, S destination, TransitionGuard<C> guard) {
        return create(source, () -> destination, guard, Action.noAction());
    }

    /**
     * Creates a transition from the specified source and destination states and the transition guard.
     *
     * @param source the source state of the transition.
     * @param destination the destination state supplier of the transition.
     * @param guard the guard for the transition.
     * @return a transition.
     */
    default Transition<S, E, C> create(S source, Supplier<S> destination, TransitionGuard<C> guard) {
        return create(source, destination, guard, Action.noAction());
    }

    /**
     * Creates a transition from the specified source and destination states and the transition action.
     *
     * @param source the source state of the transition.
     * @param destination the destination state supplier of the transition.
     * @param action the action to perform during the transition.
     * @return a transition.
     */
    default Transition<S, E, C> create(S source, S destination, Action<S, E, C> action) {
        return create(source, () -> destination, action);
    }

    /**
     * Creates a transition from the specified source and destination states and the transition action.
     *
     * @param source the source state of the transition.
     * @param destination the destination state supplier of the transition.
     * @param action the action to perform during the transition.
     * @return a transition.
     */
    default Transition<S, E, C> create(S source, Supplier<S> destination, Action<S, E, C> action) {
        return create(source, destination, TransitionGuard.none(), action);
    }

    /**
     * Creates a transition from the specified source and destination states, the transition guard, and the transition
     * action.
     *
     * @param source the source state of the transition.
     * @param destination the destination state supplier of the transition.
     * @param guard the guard for the transition.
     * @param action the action to perform during the transition.
     * @return a transition.
     */
    default Transition<S, E, C> create(S source, S destination, TransitionGuard<C> guard, Action<S, E, C> action) {
        return create(source, () -> destination, guard, action);
    }

    /**
     * Creates a transition from the specified source and destination states, the transition guard, and the transition
     * action.
     *
     * @param source the source state of the transition.
     * @param destination the destination state supplier of the transition.
     * @param guard the guard for the transition.
     * @param action the action to perform during the transition.
     * @return a transition.
     */
    Transition<S, E, C> create(S source, Supplier<S> destination, TransitionGuard<C> guard, Action<S, E, C> action);
}
