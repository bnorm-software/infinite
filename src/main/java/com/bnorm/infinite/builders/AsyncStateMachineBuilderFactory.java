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
public interface AsyncStateMachineBuilderFactory<S, E, C> {

    /**
     * Creates the default asynchronous state machine builder factory.  This is the asynchronous state machine builder
     * base constructor.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return default state machine builder factory.
     */
    static <S, E, C> AsyncStateMachineBuilderFactory<S, E, C> getDefault() {
        return AsyncStateMachineBuilderBase::new;
    }

    /**
     * Creates the default asynchronous state machine builder.  This is a combination of the state machine structure,
     * asynchronous state machine factory, asynchronous state builder factory, and asynchronous action factory.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return default state machine builder.
     */
    static <S, E, C> AsyncStateMachineBuilder<S, E, C> createDefault() {
        return AsyncStateMachineBuilderFactory.<S, E, C>getDefault()
                                              .create(StateMachineStructureFactory.createDefault(),
                                                      AsyncStateMachineFactory.getDefault(),
                                                      AsyncStateBuilderFactory.getDefault(),
                                                      AsyncActionFactory.getDefault());
    }


    /**
     * Creates a state machine builder from the specified state machine structure, asynchronous state machine factory,
     * asynchronous state builder factory, and asynchronous action factory.
     *
     * @param structure the state machine structure.
     * @param asyncStateMachineFactory the factory used to create the asynchronous state machine.
     * @param asyncStateBuilderFactory the factory used to create asynchronous state builders.
     * @param asyncActionFactory the factory used to create asynchronous actions.
     * @return a asynchronous state machine builder.
     */
    AsyncStateMachineBuilder<S, E, C> create(StateMachineStructure<S, E, C> structure,
                                             AsyncStateMachineFactory<S, E, C> asyncStateMachineFactory,
                                             AsyncStateBuilderFactory<S, E, C> asyncStateBuilderFactory,
                                             AsyncActionFactory<S, E, C> asyncActionFactory);
}
