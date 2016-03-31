package com.bnorm.infinite.file;

import com.bnorm.infinite.TransitionGuard;

/**
 * Functional interface that represents how to convert a string into a TransitionGuard.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.2.0
 */
@FunctionalInterface
public interface StringTransitionGuardFactory<S, E, C> {

    /**
     * A string transition guard factory that always returns no guard.  Since this string transition guard factory
     * always returns the type safe {@link TransitionGuard#none()}, it can be cast to any required state machine types.
     */
    StringTransitionGuardFactory<?, ?, ?> NO_GUARD_FACTORY = (s, e, d, a) -> TransitionGuard.none();

    /**
     * Returns the {@link StringTransitionGuardFactory#NO_GUARD_FACTORY} factory cast to the required parameter types.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return a type safe {@link StringTransitionGuardFactory#NO_GUARD_FACTORY} factory.
     */
    static <S, E, C> StringTransitionGuardFactory<S, E, C> noGuardFactory() {
        @SuppressWarnings("unchecked")
        StringTransitionGuardFactory<S, E, C> noGuardFactory = (StringTransitionGuardFactory<S, E, C>) NO_GUARD_FACTORY;
        return noGuardFactory;
    }

    /**
     * Creates a new transition guard given the specified guard string, transition event, and source and destination
     * states.  How to interpret the parameters is completely up to the implementing class.
     *
     * @param state the source state of the transition.
     * @param event the event which will generate the transition.
     * @param destination the destination state of the transition.
     * @param guard the string that should be converted to a guard.
     * @return a new transition guard from the guard string.
     */
    TransitionGuard<S, E, C> create(S state, E event, S destination, String guard);
}
