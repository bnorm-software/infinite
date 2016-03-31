package com.bnorm.infinite;

/**
 * Simple interface that represents a transition guard.
 *
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.0.0
 */
@FunctionalInterface
public interface ContextTransitionGuard<C> extends TransitionGuard<Object, Object, C> {

    @Override
    default boolean allowed(Object state, Object event, C context) {
        return allowed(context);
    }

    /**
     * If a transition is allowed given the specified state machine context.
     *
     * @param context the state machine context.
     * @return if the transition is currently allowed.
     */
    boolean allowed(C context);
}
