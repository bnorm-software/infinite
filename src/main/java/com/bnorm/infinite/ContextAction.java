package com.bnorm.infinite;

/**
 * Functional interface for an action that only requires the state machine context.
 *
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.0.0
 */
@FunctionalInterface
public interface ContextAction<C> extends Action<Object, Object, C> {

    @Override
    default void perform(Object state, Object event, Transition<?, ?, ? extends C> transition, C context) {
        perform(context);
    }

    /**
     * Performs a specific action with the state machine context as a parameter.
     *
     * @param context the state machine context.
     */
    void perform(C context);
}
