package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachineStructure;
import com.bnorm.infinite.StateMachineStructureFactory;
import com.bnorm.infinite.async.AsyncActionFactory;
import com.bnorm.infinite.async.AsyncStateMachineFactory;

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
     * todo
     * Creates a state machine builder from the specified state machine structure, asynchronous state machine factory,
     * asynchronous state builder factory, and asynchronous action factory.
     *
     * @param structure the state machine structure.
     * @return a asynchronous state machine builder.
     */
    @Override
    AsyncStateMachineBuilder<S, E, C> create(StateMachineStructure<S, E, C> structure);
}
