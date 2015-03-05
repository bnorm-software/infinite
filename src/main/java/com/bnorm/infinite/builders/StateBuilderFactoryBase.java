package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachineStructure;

public class StateBuilderFactoryBase<S, E, C> implements StateBuilderFactory<S, E, C> {

    @Override
    public StateBuilder<S, E, C> create(StateMachineStructure<S, E, C> structure, S state) {
        return new StateBuilderBase<>(structure, state);
    }
}
