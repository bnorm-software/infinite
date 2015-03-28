package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachineStructure;

/**
 * A factory interface for asynchronous state machine builders.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.1.0
 */
public interface AsyncStateMachineBuilderFactory<S, E, C> extends StateMachineBuilderFactory<S, E, C> {

    /**
     * Creates a state machine builder from the specified state machine structure.
     *
     * @param structure the state machine structure.
     * @return an asynchronous state machine builder.
     */
    @Override
    AsyncStateMachineBuilder<S, E, C> create(StateMachineStructure<S, E, C> structure);
}
