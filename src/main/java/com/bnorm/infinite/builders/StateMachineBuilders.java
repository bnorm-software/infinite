package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachineStructures;

/**
 * Utility class for state machine builders.
 *
 * @author Brian Norman
 * @since 1.3.0
 */
public final class StateMachineBuilders {

    /**
     * Creates a default state machine builder.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return a new state machine builder.
     */
    public static <S, E, C> StateMachineBuilder<S, E, C> create() {
        return create(new StateMachineBuilderFactoryBase<>());
    }

    /**
     * Creates an state machine builder from the specified factory.
     *
     * @param stateMachineBuilderFactory the factory used to create a state machine builder.
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return a new state machine builder.
     */
    public static <S, E, C> StateMachineBuilder<S, E, C> create(
            StateMachineBuilderFactory<S, E, C> stateMachineBuilderFactory) {
        return stateMachineBuilderFactory.create(StateMachineStructures.create());
    }
}
