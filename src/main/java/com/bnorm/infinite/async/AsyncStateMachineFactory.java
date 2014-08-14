package com.bnorm.infinite.async;

import com.bnorm.infinite.StateMachineFactory;
import com.bnorm.infinite.StateMachineStructure;

/**
 * A factory interface for asynchronous state machines.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.1.0
 */
public interface AsyncStateMachineFactory<S, E, C> extends StateMachineFactory<S, E, C> {

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
     * Creates an asynchronous state machine from the specified state machine structure, starting state, and context.
     *
     * @param structure the state machine structure.
     * @param starting the starting state of the state machine.
     * @param context the state machine context.
     * @return a state machine.
     */
    @Override
    AsyncStateMachine<S, E, C> create(StateMachineStructure<S, E, C> structure, S starting, C context);
}
