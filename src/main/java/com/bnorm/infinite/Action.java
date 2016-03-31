package com.bnorm.infinite;

/**
 * Functional interface that represents an action that should be performed.  Actions are performed when a state is
 * either entered or exited.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.0.0
 */
@FunctionalInterface
public interface Action<S, E, C> {

    /**
     * An action that does absolutely nothing.  Since this action always does nothing, it can be safely cast to any
     * required state machine types.
     */
    Action<?, ?, ?> NO_ACTION = (state, event, transition, context) -> {
    };

    /**
     * Returns the {@link Action#NO_ACTION} action cast to the required parameter types.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return a type safe {@link Action#NO_ACTION} action.
     */
    static <S, E, C> Action<S, E, C> noAction() {
        @SuppressWarnings("unchecked")
        Action<S, E, C> noAction = (Action<S, E, C>) NO_ACTION;
        return noAction;
    }

    /**
     * Performs a specific action.  The parameters specified are the conditions of the state transition.  Sometimes the
     * state being entered or existed is not a part of the specific transition - it might be a parent state of the
     * source or destination.
     *
     * @param state the state being entered or exited or the common ancestor of the states.
     * @param event the event causing the transition.
     * @param transition the exact transition taking place.
     * @param context the state machine context.
     */
    void perform(S state, E event, Transition<? extends S, ? extends E, ? extends C> transition, C context);
}
