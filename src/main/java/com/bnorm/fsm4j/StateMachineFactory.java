package com.bnorm.fsm4j;

import java.util.Map;
import java.util.Set;

/**
 * A factory interface for state machines.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public interface StateMachineFactory<S extends State, E extends Event, C extends Context> {

    /**
     * Returns the default internal state factory.  This is the internal state base constructor.
     *
     * @return default internal state factory.
     */
    static <S extends State, E extends Event, C extends Context> StateMachineFactory<S, E, C> getDefault() {
        return StateMachineBase::new;
    }

    /**
     * Creates a state machine from the specified state map, transition map, and starting state.
     *
     * @param states the states of the state machine.
     * @param transitions the transitions of the state machine.
     * @param starting the starting state of the state machine.
     * @param context the state machine context.
     * @return a state machine.
     */
    StateMachine<S, E, C> create(Map<S, InternalState<S, E, C>> states, Map<E, Set<Transition<S>>> transitions,
                                 S starting, C context);
}