package com.bnorm.infinite;

import java.util.Map;
import java.util.Set;

/**
 * A factory interface for state machines.
 *
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public interface StateMachineFactory {

    /**
     * Returns the default internal state factory.  This is the internal state base constructor.
     *
     * @return default internal state factory.
     */
    static StateMachineFactory getDefault() {
        return StateMachineBase::new;
    }

    /**
     * Creates a state machine from the specified state map, transition map, starting state, and context.
     *
     * @param states the states of the state machine.
     * @param transitions the transitions of the state machine.
     * @param starting the starting state of the state machine.
     * @param context the state machine context.
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return a state machine.
     */
    <S, E, C> StateMachine<S, E, C> create(Map<S, InternalState<S, E, C>> states,
                                           Map<E, Set<Transition<S, C>>> transitions, S starting, C context);
}
