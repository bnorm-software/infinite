package com.bnorm.fsm4j;

/**
 * Functional interface that represents an action that should be performed.  Actions are performed when a state is
 * either entered or exited.
 *
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
@FunctionalInterface
public interface Action<S extends State, E extends Event> {

    /**
     * Performs a specific action.  The parameters specified are the conditions of the state transition.  Sometimes the
     * state being entered or existed is not a part of the specific transition - it might be a parent state of the
     * source or destination.
     *
     * @param state the state being entered or exited.
     * @param event the event causing the transition.
     * @param transition the exact transition taking place.
     */
    void perform(S state, E event, Transition<S> transition);
}
