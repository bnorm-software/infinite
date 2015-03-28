package com.bnorm.infinite;

/**
 * Utility class for state machine structures.
 *
 * @author Brian Norman
 * @since 1.3.0
 */
public final class StateMachineStructures {

    /**
     * Creates a default state machine structure.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return a new state machine structure.
     */
    public static <S, E, C> StateMachineStructure<S, E, C> create() {
        StateMachineStructureFactory<S, E, C> structureFactory = new StateMachineStructureFactoryBase<>();
        return structureFactory.create();
    }
}
