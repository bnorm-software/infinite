package com.bnorm.infinite.async;

import com.bnorm.infinite.StateMachineStructure;
import com.bnorm.infinite.StateMachineStructures;

/**
 * Utility class for asynchronous state machines.
 *
 * @author Brian Norman
 * @since 1.2.1
 */
public final class AsyncStateMachines {

    /**
     * Creates a default asynchronous state machine with the specified starting state and state machine context.
     *
     * @param starting the starting state of the state machine.
     * @param context the state machine context.
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return a new state machine.
     */
    public static <S, E, C> AsyncStateMachine<S, E, C> create(S starting, C context) {
        return create(StateMachineStructures.create(), starting, context);
    }

    /**
     * Creates a default asynchronous state machine with the specified state machine structure, starting state, and
     * state machine context.
     *
     * @param structure the state machine structure.
     * @param starting the starting state of the state machine.
     * @param context the state machine context.
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return a new state machine.
     */
    public static <S, E, C> AsyncStateMachine<S, E, C> create(StateMachineStructure<S, E, C> structure, S starting,
                                                              C context) {
        return new AsyncStateMachineBase<>(structure, starting, context);
    }
}
