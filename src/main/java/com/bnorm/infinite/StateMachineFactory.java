package com.bnorm.infinite;

import java.util.Map;
import java.util.Set;

/**
 * A factory interface for state machines.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.0.1
 * @since 1.0.0
 */
public interface StateMachineFactory<S, E, C> {

    /**
     * Returns the default state machine factory.  This is the state machine base constructor.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return default state machine factory.
     */
    static <S, E, C> StateMachineFactory<S, E, C> getDefault() {
        return StateMachineBase::new;
    }

    /**
     * Creates a state machine from the specified state map, transition map, starting state, and context.
     *
     * @param transitionFactory the factory used to create transitions.
     * @param states the states of the state machine.
     * @param transitions the transitions of the state machine.
     * @param context the state machine context.
     * @param starting the starting state of the state machine.
     * @return a state machine.
     */
    StateMachine<S, E, C> create(TransitionFactory<S, C> transitionFactory, Map<S, InternalState<S, E, C>> states,
                                 Map<E, Set<Transition<S, C>>> transitions, C context, S starting);
}
