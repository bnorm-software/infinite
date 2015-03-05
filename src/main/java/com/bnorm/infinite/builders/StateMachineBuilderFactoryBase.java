package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachineFactory;
import com.bnorm.infinite.StateMachineFactoryBase;
import com.bnorm.infinite.StateMachineStructure;

public class StateMachineBuilderFactoryBase<S, E, C> implements StateMachineBuilderFactory<S, E, C> {

    protected final StateMachineFactory<S, E, C> stateMachineFactory;
    protected final StateBuilderFactory<S, E, C> stateBuilderFactory;

    public StateMachineBuilderFactoryBase(StateMachineFactory<S, E, C> stateMachineFactory,
                                          StateBuilderFactory<S, E, C> stateBuilderFactory) {
        this.stateMachineFactory = stateMachineFactory;
        this.stateBuilderFactory = stateBuilderFactory;
    }

    public StateMachineBuilderFactoryBase() {
        this(new StateMachineFactoryBase<>(), new StateBuilderFactoryBase<>());
    }

    @Override
    public StateMachineBuilder<S, E, C> create(StateMachineStructure<S, E, C> structure) {
        return new StateMachineBuilderBase<>(structure, stateMachineFactory, stateBuilderFactory);
    }
}
