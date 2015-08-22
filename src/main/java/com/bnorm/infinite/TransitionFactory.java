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
    default Transition<S, E, C> create(S source, Supplier<? extends S> destination) {
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
    default Transition<S, E, C> create(S source, S destination, TransitionGuard<? super C> guard) {
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
    default Transition<S, E, C> create(S source, Supplier<? extends S> destination, TransitionGuard<? super C> guard) {
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
    default Transition<S, E, C> create(S source, S destination, Action<? super S, ? super E, ? super C> action) {
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
    default Transition<S, E, C> create(S source, Supplier<? extends S> destination,
                                       Action<? super S, ? super E, ? super C> action) {
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
    default Transition<S, E, C> create(S source, S destination, TransitionGuard<? super C> guard,
                                       Action<? super S, ? super E, ? super C> action) {
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
    Transition<S, E, C> create(S source, Supplier<? extends S> destination, TransitionGuard<? super C> guard,
                               Action<? super S, ? super E, ? super C> action);
}
