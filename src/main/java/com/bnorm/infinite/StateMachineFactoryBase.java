package com.bnorm.infinite;

public class StateMachineFactoryBase<S, E, C> implements StateMachineFactory<S, E, C> {

    @Override
    public StateMachine<S, E, C> create(StateMachineStructure<S, E, C> structure, S starting, C context) {
        return new StateMachineBase<>(structure, starting, context);
    }
}
