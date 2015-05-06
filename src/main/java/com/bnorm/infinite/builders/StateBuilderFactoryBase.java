package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachineStructure;

/**
 * The base implementation of a state builder factory.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.3.0
 */
public class StateBuilderFactoryBase<S, E, C> implements StateBuilderFactory<S, E, C> {

    @Override
    public StateBuilder<S, E, C> create(StateMachineStructure<S, E, C> structure, S state) {
        return new StateBuilderBase<>(structure, state);
    }
}
