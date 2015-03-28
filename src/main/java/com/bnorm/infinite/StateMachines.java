package com.bnorm.infinite;

/**
 * Utility class for state machines.
 *
 * @author Brian Norman
 * @since 1.3.0
 */
public final class StateMachines {

    /**
     * Creates a default state machine with the specified starting state and state machine context.
     *
     * @param starting the starting state of the state machine.
     * @param context the state machine context.
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return a new state machine.
     */
    public static <S, E, C> StateMachine<S, E, C> create(S starting, C context) {
        return create(StateMachineStructures.create(), starting, context);
    }

    /**
     * Creates a default state machine with the specified state machine structure, starting state, and state machine
     * context.
     *
     * @param structure the state machine structure.
     * @param starting the starting state of the state machine.
     * @param context the state machine context.
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return a new state machine.
     */
    public static <S, E, C> StateMachine<S, E, C> create(StateMachineStructure<S, E, C> structure, S starting,
                                                         C context) {
        return new StateMachineBase<>(structure, starting, context);
    }
}
