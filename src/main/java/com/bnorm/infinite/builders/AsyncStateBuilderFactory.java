package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachineStructure;

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

    @Override
    AsyncStateBuilder<S, E, C> create(StateMachineStructure<S, E, C> structure, S state);
}
