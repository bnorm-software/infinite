package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachineStructure;
import com.bnorm.infinite.async.AsyncActionFactory;

/**
 * A factory interface for asynchronous state builders.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.1.0
 */
public interface AsyncStateBuilderFactory<S, E, C> extends StateBuilderFactory<S, E, C> {

    /**
     * Returns the default asynchronous state builder factory.  This is the state builder base constructor.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return default state builder factory.
     */
    static <S, E, C> AsyncStateBuilderFactory<S, E, C> getDefault() {
        return AsyncStateBuilderBase::new;
    }


    @Override
    default AsyncStateBuilder<S, E, C> create(StateMachineStructure<S, E, C> structure, S state) {
        return create(structure, state, AsyncActionFactory.getDefault());
    }

    /**
     * Creates a new state builder from the specified state machine structure, the state being built, and asynchronous
     * action factory.
     *
     * @param structure the state machine structure.
     * @param state the state being built.
     * @param asyncActionFactory the factory used to create asynchronous actions.
     * @return a new state builder.
     */
    AsyncStateBuilder<S, E, C> create(StateMachineStructure<S, E, C> structure, S state,
                                      AsyncActionFactory<S, E, C> asyncActionFactory);
}
