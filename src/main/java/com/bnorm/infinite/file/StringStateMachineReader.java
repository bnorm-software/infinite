package com.bnorm.infinite.file;

import com.bnorm.infinite.Action;
import com.bnorm.infinite.ActionType;
import com.bnorm.infinite.TransitionGuard;

/**
 * Interface that represents how to convert strings into state machine actions and transition guards.  This means that
 * it is usually a combination of a {@link StringStateActionFactory}, {@link StringTransitionActionFactory}, and [@link
 * StringTransitionGuardFactory}.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.2.0
 */
public interface StringStateMachineReader<S, E, C> {

    /**
     * Reads the specified state string and returns a typed state machine state.
     *
     * @param state the state string.
     * @return a typed state machine state.
     */
    S readState(String state);

    /**
     * Reads the specified event string and returns a typed state machine event.
     *
     * @param event the event string.
     * @return a typed state machine event.
     */
    E readEvent(String event);

    /**
     * Reads the specified state action string and returns a typed state machine action.
     *
     * @param state the action state.
     * @param type the action type.
     * @param action the state action string.
     * @return a typed state action.
     */
    Action<S, E, C> readStateAction(S state, ActionType type, String action);

    /**
     * Reads the specified transition action string and returns a typed state machine transition action.
     *
     * @param state the transition source state.
     * @param event the transition event.
     * @param destination the transition destination state.
     * @param action the transition action string.
     * @return a typed transition action.
     */
    Action<S, E, C> readTransitionAction(S state, E event, S destination, String action);

    /**
     * Reads the specified transition guard string and returns a typed state machine transition guard.
     *
     * @param state the transition source state.
     * @param event the transition event.
     * @param destination the transition destination state.
     * @param guard the transition guard string.
     * @return a typed transition guard.
     */
    TransitionGuard<C> readTransitionGuard(S state, E event, S destination, String guard);
}
