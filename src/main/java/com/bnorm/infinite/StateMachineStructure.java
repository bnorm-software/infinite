package com.bnorm.infinite;

import java.util.Set;

/**
 * A structure class representing states and transitions between them.  This structure does not have any state machine
 * like behavior, just a representation of a state machine.  This structure can be safely used between multiple state
 * machines which reduces the memory requirements for having many state machines of the same structure.
 *
 * @param <S>
 * @param <E>
 * @param <C>
 * @author Brian Norman
 * @since 1.1.0
 */
public interface StateMachineStructure<S, E, C> {

    /**
     * Returns the internal state factory.  The factory should be used to create any internal states needed by the state
     * machine.
     *
     * @return the internal state factory.
     */
    InternalStateFactory<S, E, C> getInternalStateFactory();

    /**
     * Returns the internal state associated with the specified state.  This method should never return a {@code null}
     * internal state.  If needed, the structure should use the internal state factory and create a new internal state.
     *
     * @param state the state machine state.
     * @return the associated internal state.
     */
    InternalState<S, E, C> getState(S state);

    /**
     * Returns the transition factory.  The factory should be used to create any transitions needed by the state
     * machine.
     *
     * @return the transition factory.
     */
    TransitionFactory<S, C> getTransitionFactory();

    /**
     * Returns the transitions associated with the specified event.  This method should never return a {@code null} set
     * of transitions.  If needed, the structure should create a new, empty set.
     *
     * @param event the state machine event.
     * @return the associated transitions.
     */
    Set<Transition<S, C>> getTransitions(E event);

    /**
     * Adds the specified transition as a possible transition for the specified event.
     *
     * @param event the state machine event.
     * @param transition the event transition.
     */
    void addTransition(E event, Transition<S, C> transition);
}
