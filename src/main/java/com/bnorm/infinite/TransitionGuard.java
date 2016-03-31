package com.bnorm.infinite;

/**
 * Simple interface that represents a transition guard.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.0.0
 */
@FunctionalInterface
public interface TransitionGuard<S, E, C> {

    /**
     * A transition guard that always allows the transition.  Since this guard always returns true, it can be safely
     * cast to any required state machine context type.
     */
    TransitionGuard<?, ?, ?> NONE = (state, event, context) -> true;

    /**
     * Returns the {@link TransitionGuard#NONE} transition guard cast to the required parameter type.
     *
     * @param <C> the class type of the context.
     * @return a type safe {@link TransitionGuard#NONE} transition guard.
     */
    static <S, E, C> TransitionGuard<S, E, C> none() {
        @SuppressWarnings("unchecked")
        TransitionGuard<S, E, C> guard = (TransitionGuard<S, E, C>) NONE;
        return guard;
    }

    /**
     * If a transition is allowed given the specified state machine source state, event, and context.
     *
     * @param state the state being exited.
     * @param event the event causing the transition.
     * @param context the state machine context.
     * @return if the transition is currently allowed.
     */
    boolean allowed(S state, E event, C context);
}
