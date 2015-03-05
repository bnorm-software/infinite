package com.bnorm.infinite;

/**
 * A factory interface for state machine structures.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.1.0
 */
public interface StateMachineStructureFactory<S, E, C> {

    /**
     * todo
     * Creates a new state machine structure from the specified internal state factory and transition factory.
     *
     * @return a new state machine structure.
     */
    StateMachineStructure<S, E, C> create();
}
