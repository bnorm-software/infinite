package com.bnorm.infinite;

/**
 * Simple interface that represents a transition guard.
 *
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
@FunctionalInterface
public interface TransitionGuard<C> {

    /**
     * A transition guard that always allows the transition.  Since this guard always returns true, it can be safely
     * cast to any required state machine context type.
     */
    static TransitionGuard<?> NONE = context -> true;

    /**
     * Returns the {@link TransitionGuard#NONE} transition guard cast to the required parameter type.
     *
     * @param <C> the class type of the context.
     * @return a type safe {@link TransitionGuard#NONE} transition guard.
     */
    static <C> TransitionGuard<C> none() {
        @SuppressWarnings("unchecked")
        TransitionGuard<C> guard = (TransitionGuard<C>) NONE;
        return guard;
    }

    /**
     * If a transition is allowed given the specified state machine context.
     *
     * @param context the state machine context.
     * @return if the transition is currently allowed.
     */
    boolean allowed(C context);
}
