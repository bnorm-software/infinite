package com.bnorm.infinite.async;

import java.util.Map;
import java.util.Set;

import com.bnorm.infinite.InternalState;
import com.bnorm.infinite.StateMachineFactory;
import com.bnorm.infinite.Transition;
import com.bnorm.infinite.TransitionFactory;

/**
 * A factory interface for asynchronous state machines.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.1.0
 * @since 1.1.0
 */
public interface AsyncStateMachineFactory<S, E, C> {

    /**
     * Returns the default asynchronous state machine factory.  This is the asynchronous state machine base
     * constructor.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return default internal state factory.
     */
    static <S, E, C> AsyncStateMachineFactory<S, E, C> getDefault() {
        return AsyncStateMachineBase::new;
    }

    /**
     * Creates an asynchronous state machine from the specified transition factory, state map, transition map, starting
     * state, and context.
     *
     * @param transitionFactory the factory used to create transitions.
     * @param states the states of the state machine.
     * @param transitions the transitions of the state machine.
     * @param starting the starting state of the state machine.
     * @param context the state machine context.
     * @return a state machine.
     */
    AsyncStateMachine<S, E, C> create(TransitionFactory<S, C> transitionFactory, Map<S, InternalState<S, E, C>> states,
                                      Map<E, Set<Transition<S, C>>> transitions, S starting, C context);
}
